import React, { useEffect, useState } from 'react';
import '../styles/Dashboard.css';
import KPICard from '../components/KPICard';
import StatusChart from '../components/StatusChart';
import { kpiService, fusionService } from '../services/api';

const Dashboard = () => {
    const [kpis, setKpis] = useState(null);
    const [alertes, setAlertes] = useState([]);
    const [residents, setResidents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                console.log('🔄 Début du chargement des données...');

                const [kpiRes, residentRes, alertesRes] = await Promise.all([
                    kpiService.getDashboard(),
                    fusionService.getUnifiedResidents(),
                    kpiService.getAlertes()
                ]);

                console.log('📊 Réponse KPI:', kpiRes);
                console.log('👥 Réponse Résidents:', residentRes);
                console.log('🚨 Réponse Alertes:', alertesRes);

                setKpis(kpiRes.data);
                setResidents(Array.isArray(residentRes.data) ? residentRes.data : []);
                setAlertes(Array.isArray(alertesRes.data) ? alertesRes.data : []);

                setError(null);
                console.log('✅ Données chargées avec succès');
            } catch (err) {
                console.error('❌ Erreur chargement:', err);
                console.error('❌ Message d\'erreur:', err.message);
                console.error('❌ Réponse serveur:', err.response?.data);
                console.error('❌ Statut:', err.response?.status);

                let errorMessage = "Erreur de chargement des données du tableau de bord.";
                if (err.response?.status === 404) {
                    errorMessage = "Service non trouvé. Vérifiez que le serveur backend est démarré.";
                } else if (err.response?.status >= 500) {
                    errorMessage = "Erreur serveur. Contactez l'administrateur.";
                } else if (err.code === 'ECONNREFUSED') {
                    errorMessage = "Impossible de se connecter au serveur. Vérifiez que le serveur backend est démarré sur le port 8081.";
                }

                setError(errorMessage);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    if (loading) return <div className="loading">Chargement...</div>;
    if (error) return <div className="error-message">{error}</div>;

    const total = kpis?.totalResidents?.count || 0;
    const actifs = kpis?.statutDistribution?.data?.find(d => d.status === 'ACTIF')?.count || 0;
    const inactifs = kpis?.statutDistribution?.data?.find(d => d.status === 'INACTIF')?.count || 0;
    const taux = kpis?.inactiveRate?.rate || 0;

    const statusData = { actif: actifs, inactif: inactifs };

    return (
        <div className="dashboard">
            <h1>Tableau de bord des résidents</h1>

            <div className="kpi-grid">
                <KPICard icon="👥" label="Total Résidents" value={total} color="primary" />
                <KPICard icon="✅" label="Résidents Actifs" value={actifs} color="success" />
                <KPICard icon="❌" label="Résidents Inactifs" value={inactifs} color="warning" />
                <KPICard icon="📊" label="Taux d'inactivité" value={`${taux.toFixed(1)}%`} color="info" />
            </div>

            <div className="charts-container">
                <div className="chart-section">
                    <h2>Statut des comptes</h2>
                    <StatusChart data={statusData} />
                </div>

                <div className="alertes-container">
                    <h2>Alertes</h2>
                    {alertes.length > 0 ? (
                        <ul className="alertes-list">
                            {alertes.map((alerte, index) => (
                                <li key={index} className={`alert-item ${alerte.priority?.toLowerCase() || 'low'}`}>
                                    <div className="alert-content">
                                        <div className="alert-message">{alerte.message}</div>
                                        <div className="alert-meta">
                                            <span className={`alert-priority priority-${alerte.priority?.toLowerCase() || 'low'}`}>
                                                {alerte.priority || 'LOW'}
                                            </span>
                                            <span className="alert-date">
                                                {alerte.date || new Date().toLocaleDateString()}
                                            </span>
                                        </div>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <div className="no-alerts">
                            Aucune alerte active
                        </div>
                    )}
                </div>
            </div>

            <div className="table-container">
                <h2>Liste des résidents</h2>
                {residents.length === 0 ? (
                    <p>Aucun résident trouvé.</p>
                ) : (
                    <table className="residents-table">
                        <thead>
                        <tr>
                            <th>Nom</th>
                            <th>Matricule</th>
                            <th>Email</th>
                            <th>Service</th>
                            <th>Statut Global</th>
                            <th>Type</th>
                        </tr>
                        </thead>
                        <tbody>
                        {residents.map((r, index) => (
                            <tr key={r.id || r.matricule || index}>
                                <td>{r.fullName || 'N/A'}</td>
                                <td>{r.matricule || 'N/A'}</td>
                                <td>{r.email || 'N/A'}</td>
                                <td>{r.service || 'N/A'}</td>
                                <td>
                                    <span className={`status-badge status-${r.statutGlobal?.toLowerCase()}`}>
                                        {r.statutGlobal || 'N/A'}
                                    </span>
                                </td>
                                <td>
                                    <span className={`service-badge service-${r.typeService?.toLowerCase()}`}>
                                        {r.typeService || 'N/A'}
                                    </span>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                )}
            </div>
        </div>
    );
};

export default Dashboard;
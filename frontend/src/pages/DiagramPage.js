import React, { useEffect, useState } from 'react';
import '../styles/DiagramPage.css'
import StatusChart from '../components/StatusChart';
import DepartmentChart from '../components/DepartementChart';
import { kpiService } from '../services/api';

const DiagramPage = () => {
    const [statusData, setStatusData] = useState(null);
    const [departmentData, setDepartmentData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        loadKPIData();
    }, []);

    const loadKPIData = async () => {
        try {
            setLoading(true);
            // Utiliser getDashboard au lieu de getCharts qui n'existe pas
            const response = await kpiService.getDashboard();
            const data = response.data;

            console.log('Données KPI reçues:', data);

            // Transformation des données pour StatusChart
            const actifs = data.statutDistribution?.data?.find(d => d.status === 'ACTIF')?.count || 0;
            const inactifs = data.statutDistribution?.data?.find(d => d.status === 'INACTIF')?.count || 0;

            setStatusData({
                actif: actifs,
                inactif: inactifs
            });

            // Transformation des données pour DepartmentChart
            setDepartmentData(data.departmentChart?.data || {
                chirurgie: 0,
                pediatrie: 0,
                cardiologie: 0
            });

            setError(null);
        } catch (error) {
            console.error('Erreur de chargement des KPIs:', error);
            setError('Erreur lors du chargement des données statistiques');
        } finally {
            setLoading(false);
        }
    };

    const handleRefresh = () => {
        loadKPIData();
    };

    if (loading) {
        return <div className="loading">Chargement des diagrammes statistiques...</div>;
    }

    if (error) {
        return (
            <div className="error-container">
                <div className="error-message">{error}</div>
                <button onClick={handleRefresh} className="btn-primary">
                    🔄 Réessayer
                </button>
            </div>
        );
    }

    return (
        <div className="diagram-page">
            <div className="page-header">
                <h1>Diagrammes Statistiques</h1>
                <button onClick={handleRefresh} className="btn-secondary">
                    🔄 Actualiser
                </button>
            </div>

            <div className="charts-container">
                <div className="chart-section">
                    <StatusChart data={statusData} />
                </div>
                <div className="chart-section">
                    <DepartmentChart data={departmentData} />
                </div>
            </div>

            {/* Résumé des données */}
            <div className="data-summary">
                <h3>Résumé des données</h3>
                <div className="summary-grid">
                    <div className="summary-item">
                        <strong>Résidents actifs:</strong> {statusData?.actif || 0}
                    </div>
                    <div className="summary-item">
                        <strong>Résidents inactifs:</strong> {statusData?.inactif || 0}
                    </div>
                    <div className="summary-item">
                        <strong>Total résidents:</strong> {(statusData?.actif || 0) + (statusData?.inactif || 0)}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default DiagramPage;
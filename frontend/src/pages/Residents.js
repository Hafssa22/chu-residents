import React, { useEffect, useRef, useState } from 'react';
import '../styles/Residents.css';
import { useNavigate, useLocation } from 'react-router-dom';
import {
    fusionService,
    residentsService,
    pdfService,
    desactivationService,
    migrationService, searchResidents
} from '../services/api';

const useFusion = true;

const ResidentsPage = () => {
    const [residents, setResidents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [message, setMessage] = useState(null);
    const navigate = useNavigate();
    const location = useLocation();
    const tableRef = useRef(null);

    useEffect(() => {
        if (location.state?.message) {
            setMessage(location.state.message);
            navigate(location.pathname, { replace: true });
        }
    }, [location, navigate]);

    const fetchResidents = async () => {
        try {
            setLoading(true);
            setError(null);
            const response = useFusion
                ? await fusionService.getUnifiedResidents()
                : await residentsService.getAllResidents();
            setResidents(Array.isArray(response.data) ? response.data : []);
        } catch (err) {
            console.error('Erreur de chargement des résidents:', err);
            setError('Erreur lors du chargement de la liste des résidents');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchResidents();
    }, []);

    const handleDelete = async (id, fullName) => {
        if (!window.confirm(`Êtes-vous sûr de vouloir supprimer ${fullName} ?`)) return;
        try {
            await residentsService.delete(id);
            setMessage('Résident supprimé avec succès');
            await fetchResidents();
        } catch (err) {
            console.error('Erreur de suppression:', err);
            setError('Erreur lors de la suppression du résident');
        }
    };

    const handlePDF = async (matricule, fullName) => {
        try {
            const response = await pdfService.generate(matricule);
            const url = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `fiche_${matricule}_${fullName}.pdf`);
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            window.URL.revokeObjectURL(url);
            setMessage('PDF téléchargé avec succès');
        } catch (err) {
            console.error('Erreur de génération PDF:', err);
            setError('Erreur lors de la génération du PDF');
        }
    };

    const triggerDesactivation = async () => {
        try {
            await desactivationService.triggerDesactivation();
            setMessage('Désactivation automatique déclenchée');
            await fetchResidents();
        } catch (err) {
            console.error('Erreur désactivation:', err);
            setError('Erreur lors de la désactivation automatique');
        }
    };

    const triggerMigration = async () => {
        try {
            await migrationService.triggerMigration();
            setMessage('Migration déclenchée avec succès');
            await fetchResidents();
        } catch (err) {
            console.error('Erreur migration:', err);
            setError('Erreur lors de la migration');
        }
    };

    const scrollLeft = () => {
        if (tableRef.current) {
            tableRef.current.scrollBy({ left: -300, behavior: 'smooth' });
        }
    };

    const scrollRight = () => {
        if (tableRef.current) {
            tableRef.current.scrollBy({ left: 300, behavior: 'smooth' });
        }
    };

    const [searchQuery, setSearchQuery] = useState('');

    const handleSearch = async () => {
        try {
            const data = await searchResidents(searchQuery);
            setResidents(data); // <- remplace la liste des résidents affichés
        } catch (error) {
            console.error("Erreur lors de la recherche :", error);
        }
    };



    return (
        <div className="residents-page">
            <div className="page-header">
                <h1>Liste des Résidents</h1>
                <div className="page-actions">
                    <button onClick={() => navigate('/residents/add')} className="btn-primary">
                        Ajouter un résident
                    </button>
                    <button onClick={triggerDesactivation} className="btn-secondary" style={{ marginLeft: 10 }}>
                        Désactivation des comptes
                    </button>
                    <button onClick={triggerMigration} className="btn-secondary" style={{ marginLeft: 10 }}>
                        Lancer migration
                    </button>
                </div>
            </div>

            <div className="search-bar">
                <input
                    type="text"
                    placeholder="Rechercher par nom, matricule, email, service..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                />
                <button onClick={handleSearch}>Rechercher</button>
            </div>

            <div className="table-scroll-controls">
                <button onClick={scrollLeft} className="scroll-btn">⬅️</button>
                <button onClick={scrollRight} className="scroll-btn">➡️</button>
            </div>

            {loading && <p>Chargement en cours...</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {message && <p style={{ color: 'green' }}>{message}</p>}

            {!loading && !error && residents.length === 0 && (
                <p>Aucun résident trouvé.</p>
            )}

            {!loading && !error && residents.length > 0 && (
                <div className="table-container" ref={tableRef}>
                    <table className="residents-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nom</th>
                            <th>Matricule</th>
                            <th>Email</th>
                            <th>Téléphone</th>
                            <th>Service</th>
                            <th>Niveau</th>
                            <th>Poste</th>
                            <th>Service Affectation</th>
                            <th>Date entrée</th>
                            <th>Date affectation</th>
                            <th>Statut AGiRH</th>
                            <th>Statut HOSIX</th>
                            <th>Statut Global</th>
                            <th>Type Service</th>
                            <th>Date Création</th>
                            <th>Date Modification</th>
                            <th>Créé le</th>
                            <th>Mis à jour le</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {residents.map(resident => (
                            <tr key={resident.id || resident.matricule}>
                                <td>{resident.id || 'N/A'}</td>
                                <td>{resident.fullName || 'N/A'}</td>
                                <td>{resident.matricule || 'N/A'}</td>
                                <td>{resident.email || 'N/A'}</td>
                                <td>{resident.telephone || 'N/A'}</td>
                                <td>{resident.service || 'N/A'}</td>
                                <td>{resident.niveau || 'N/A'}</td>
                                <td>{resident.posteOccupe || 'N/A'}</td>
                                <td>{resident.serviceAffectation || 'N/A'}</td>
                                <td>{resident.dateDebut ? new Date(resident.dateDebut).toLocaleDateString('fr-FR') : 'N/A'}</td>
                                <td>{resident.dateAffectation ? new Date(resident.dateAffectation).toLocaleDateString('fr-FR') : 'N/A'}</td>
                                <td>{resident.statutAgirh || 'N/A'}</td>
                                <td>{resident.statutHosix || 'N/A'}</td>
                                <td>
                                    <span className={`status-badge status-${resident.statutGlobal?.toLowerCase()}`}>
                                        {resident.statutGlobal || 'N/A'}
                                    </span>
                                </td>
                                <td>
                                    <span className={`service-badge service-${resident.typeService?.toLowerCase()}`}>
                                        {resident.typeService || 'N/A'}
                                    </span>
                                </td>
                                <td>{resident.dateCreation ? new Date(resident.dateCreation).toLocaleDateString('fr-FR') : 'N/A'}</td>
                                <td>{resident.dateModification ? new Date(resident.dateModification).toLocaleDateString('fr-FR') : 'N/A'}</td>
                                <td>{resident.createdAt ? new Date(resident.createdAt).toLocaleString('fr-FR') : 'N/A'}</td>
                                <td>{resident.updatedAt ? new Date(resident.updatedAt).toLocaleString('fr-FR') : 'N/A'}</td>
                                <td className="actions-cell">
                                    <button onClick={() => navigate(`/residents/edit/${resident.id}`)} className="btn-sm btn-secondary" title="Modifier">✏️</button>
                                    <button onClick={() => handleDelete(resident.id, resident.fullName)} className="btn-sm btn-danger" title="Supprimer">🗑️</button>
                                    <button onClick={() => handlePDF(resident.id, resident.fullName)} className="btn-sm btn-info" title="Télécharger PDF">📄</button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};

export default ResidentsPage;

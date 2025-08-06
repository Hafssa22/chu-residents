import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { residentsService } from '../services/api';

const EditResident = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        fullName: '',
        typeService: '',
        dateDebut: '',
        statutGlobal: ''
    });
    const [loading, setLoading] = useState(false);
    const [loadingData, setLoadingData] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        loadResident();
    }, [id]);

    const loadResident = async () => {
        try {
            setLoadingData(true);
            const response = await residentsService.getById(id);
            setFormData(response.data);
            setError(null);
        } catch (err) {
            console.error('Erreur de chargement:', err);
            setError('Erreur lors du chargement des données du résident');
        } finally {
            setLoadingData(false);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
        // Réinitialiser l'erreur quand l'utilisateur modifie les données
        if (error) setError(null);
    };

    const validateForm = () => {
        if (!formData.fullName?.trim()) {
            setError('Le nom complet est obligatoire');
            return false;
        }
        if (!formData.typeService) {
            setError('Le type de service est obligatoire');
            return false;
        }
        if (!formData.dateDebut) {
            setError('La date de début est obligatoire');
            return false;
        }
        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validateForm()) return;

        try {
            setLoading(true);
            setError(null);
            await residentsService.update(id, formData);
            navigate('/residents', {
                state: { message: 'Résident modifié avec succès!' }
            });
        } catch (err) {
            console.error('Erreur lors de la mise à jour:', err);
            setError(
                err.response?.data?.message ||
                'Erreur lors de la mise à jour du résident'
            );
        } finally {
            setLoading(false);
        }
    };

    const handleCancel = () => {
        navigate('/residents');
    };

    if (loadingData) {
        return <div className="loading">Chargement des données du résident...</div>;
    }

    return (
        <div className="form-container">
            <h2>Modifier le résident</h2>

            {error && <div className="error-message">{error}</div>}

            <form onSubmit={handleSubmit} className="resident-form">
                {/* Nom complet */}
                <div className="form-group">
                    <label htmlFor="fullName">Nom complet *</label>
                    <input type="text" name="fullName" value={formData.fullName} onChange={handleChange} required />
                </div>

                {/* Matricule */}
                <div className="form-group">
                    <label htmlFor="matricule">Matricule *</label>
                    <input type="text" name="matricule" value={formData.matricule} onChange={handleChange} required />
                </div>

                {/* Email */}
                <div className="form-group">
                    <label htmlFor="email">Email</label>
                    <input type="email" name="email" value={formData.email} onChange={handleChange} />
                </div>

                {/* Téléphone */}
                <div className="form-group">
                    <label htmlFor="telephone">Téléphone</label>
                    <input type="tel" name="telephone" value={formData.telephone} onChange={handleChange} />
                </div>

                {/* Service */}
                <div className="form-group">
                    <label htmlFor="service">Service</label>
                    <input type="text" name="service" value={formData.service} onChange={handleChange} />
                </div>

                {/* Niveau */}
                <div className="form-group">
                    <label htmlFor="niveau">Niveau</label>
                    <input type="text" name="niveau" value={formData.niveau} onChange={handleChange} />
                </div>

                {/* Poste occupé */}
                <div className="form-group">
                    <label htmlFor="posteOccupe">Poste occupé</label>
                    <input type="text" name="posteOccupe" value={formData.posteOccupe} onChange={handleChange} />
                </div>

                {/* Service affectation */}
                <div className="form-group">
                    <label htmlFor="serviceAffectation">Service affectation</label>
                    <input type="text" name="serviceAffectation" value={formData.serviceAffectation} onChange={handleChange} />
                </div>

                {/* Date de début */}
                <div className="form-group">
                    <label htmlFor="dateDebut">Date de début *</label>
                    <input type="date" name="dateDebut" value={formData.dateDebut} onChange={handleChange} required />
                </div>

                {/* Date affectation */}
                <div className="form-group">
                    <label htmlFor="dateAffectation">Date affectation</label>
                    <input type="date" name="dateAffectation" value={formData.dateAffectation} onChange={handleChange} />
                </div>

                {/* Date création */}
                <div className="form-group">
                    <label htmlFor="dateCreation">Date de création</label>
                    <input type="date" name="dateCreation" value={formData.dateCreation} onChange={handleChange} />
                </div>

                {/* Date modification */}
                <div className="form-group">
                    <label htmlFor="dateModification">Date de modification</label>
                    <input type="date" name="dateModification" value={formData.dateModification} onChange={handleChange} />
                </div>

                {/* Type de service */}
                <div className="form-group">
                    <label htmlFor="typeService">Type de service *</label>
                    <select name="typeService" value={formData.typeService} onChange={handleChange} required>
                        <option value="">-- Sélectionnez --</option>
                        <option value="CHIRURGICAL">Chirurgical</option>
                        <option value="MEDICAL">Médical</option>
                    </select>
                </div>



                {/* Statut AGIRH */}
                <div className="form-group">
                    <label htmlFor="statutAgirh">Statut AGiRH</label>
                    <select name="statutAgirh" value={formData.statutAgirh} onChange={handleChange}>
                        <option value="ACTIF">Actif</option>
                        <option value="INACTIF">Inactif</option>
                    </select>
                </div>

                {/* Statut HOSIX */}
                <div className="form-group">
                    <label htmlFor="statutHosix">Statut HOSIX</label>
                    <select name="statutHosix" value={formData.statutHosix} onChange={handleChange}>
                        <option value="ACTIF">Actif</option>
                        <option value="INACTIF">Inactif</option>
                    </select>
                </div>

                {/* Statut global */}
                <div className="form-group">
                    <label htmlFor="statutGlobal">Statut Global</label>
                    <select name="statutGlobal" value={formData.statutGlobal} onChange={handleChange}>
                        <option value="ACTIF">Actif</option>
                        <option value="INACTIF">Inactif</option>
                    </select>
                </div>

                {/* Actions */}
                <div className="form-actions">
                    <button type="button" onClick={handleCancel} className="btn-secondary" disabled={loading}>
                        Annuler
                    </button>
                    <button type="submit" className="btn-primary" disabled={loading}>
                        {loading ? 'Ajout en cours...' : 'Ajouter le résident'}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default EditResident;
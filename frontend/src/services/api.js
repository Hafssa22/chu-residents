import axios from 'axios';

const API_BASE = 'http://localhost:8081';

// Configuration axios globale
const api = axios.create({
    baseURL: API_BASE,
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
    }
});

// Intercepteur pour gestion d'erreurs globale
api.interceptors.response.use(
    (response) => response,
    (error) => {
        console.error('Erreur API:', error);
        return Promise.reject(error);
    }
);

// Services KPI - CORRIGÉ: utilise l'instance 'api'
export const kpiService = {
    getDashboard: () => api.get('/api/kpi/dashboard'),
    getAlertes: () => api.get('/api/kpi/alertes')
};

// Services Résidents
export const residentsService = {
    getAllResidents: () => api.get('/api/residents/residents'),
    getById: (id) => api.get(`/api/residents/read/${id}`),
    create: (resident) => api.post('/api/residents/create', resident),
    update: (id, resident) => api.put(`/api/residents/update/${id}`, resident),
    delete: (id) => api.delete(`/api/residents/delete/${id}`),
};

// CORRIGÉ: utilise l'instance 'api'
export const searchResidents = async (query) => {
    try {
        const response = await api.get(`/api/residents/search?query=${query}`);
        return response.data;
    } catch (error) {
        console.error("Erreur lors de la recherche :", error);
        throw error;
    }
};

// Services Fusion
export const fusionService = {
    getUnifiedResidents: () => api.get('/fusion/chu')
};

// Services PDF
export const pdfService = {
    generate: (id) => api.get(`/pdf/fiche/${id}`, { responseType: 'blob' }),
};

// Services Migration
export const migrationService = {
    migrate: () => api.post('/migrate-residents'),
    triggerMigration: () => api.post('/migrate-residents')
};

// Services Désactivation
export const desactivationService = {
    triggerDesactivation: () => api.post('/residents/desactiver-inactifs')
};

export { api };
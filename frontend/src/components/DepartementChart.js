import React from 'react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, CartesianGrid, ResponsiveContainer } from 'recharts';

const DepartmentChart = ({ data }) => {
    if (!data) {
        return (
            <div className="chart-box">
                <h3>Répartition par département</h3>
                <p>Aucune donnée disponible</p>
            </div>
        );
    }

    // Transformation des données pour le graphique
    const chartData = [
        { name: 'Chirurgie', value: data.chirurgie ?? 0 },
        { name: 'Pédiatrie', value: data.pediatrie ?? 0 },
        { name: 'Cardiologie', value: data.cardiologie ?? 0 }
    ];

    return (
        <div className="chart-box">
            <h3>Répartition par département</h3>
            <ResponsiveContainer width="100%" height={300}>
                <BarChart data={chartData} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="name" />
                    <YAxis />
                    <Tooltip />
                    <Bar dataKey="value" fill="#8884d8" />
                </BarChart>
            </ResponsiveContainer>
        </div>
    );
};

export default DepartmentChart;
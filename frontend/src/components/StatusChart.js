import React from 'react';
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer, Legend } from 'recharts';

const COLORS = ['#00C49F', '#FF8042'];

const StatusChart = ({ data }) => {
    if (!data) {
        return (
            <div className="chart-box">
                <h3>Statuts des comptes</h3>
                <p>Aucune donnée disponible</p>
            </div>
        );
    }

    // Transformation des données pour le graphique
    const chartData = [
        { name: 'Actifs', value: data.actif ?? 0 },
        { name: 'Inactifs', value: data.inactif ?? 0 }
    ];

    return (
        <div className="chart-box">
            <h3>Statuts des comptes</h3>
            <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                    <Pie
                        data={chartData}
                        dataKey="value"
                        nameKey="name"
                        cx="50%"
                        cy="50%"
                        outerRadius={80}
                        label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                    >
                        {chartData.map((entry, index) => (
                            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                        ))}
                    </Pie>
                    <Tooltip />
                    <Legend />
                </PieChart>
            </ResponsiveContainer>
        </div>
    );
};

export default StatusChart;
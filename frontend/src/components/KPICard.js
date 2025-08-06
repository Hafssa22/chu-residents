import React from 'react';

const KPICard = ({ icon, value, label, color = 'primary' }) => {
    return (
        <div className={`kpi-card kpi-${color}`}>
            {icon && <div className="kpi-icon">{icon}</div>}
            <div className="kpi-info">
                <h3 className="kpi-value">{value ?? 0}</h3>
                <p className="kpi-label">{label}</p>
            </div>
        </div>
    );
};

export default KPICard;
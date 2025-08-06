import React from 'react';
import {Link, NavLink} from 'react-router-dom';

const Navigation = () => {
    return (
        <header className="header">
            <nav>
                <ul className="nav-links">
                    <li>
                        <NavLink
                            to="/"
                            end
                            className={({ isActive }) => isActive ? 'active' : ''}
                        >
                             Dashboard
                        </NavLink>
                    </li>
                    <li>
                        <NavLink
                            to="/residents"
                            className={({ isActive }) => isActive ? 'active' : ''}
                        >
                             Liste Résidents
                        </NavLink>
                    </li>
                    <li>
                        <NavLink
                            to="/diagrams"
                            className={({ isActive }) => isActive ? 'active' : ''}
                        >
                             Diagrammes
                        </NavLink>
                    </li>
                    <li>
                        <Link to="/chatbot" className="nav-link">Assistant Interne</Link>

                    </li>
                </ul>
            </nav>
        </header>
    );
};

export default Navigation;
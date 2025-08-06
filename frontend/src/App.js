import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Navigation from './components/Navigation';
import Dashboard from './pages/Dashboard';
import DiagramPage from './pages/DiagramPage';
import ResidentsPage from './pages/Residents';
import AddResident from './pages/AddResident';
import EditResident from './pages/EditResident';
import './App.css';
import ChatbotFAQ from './pages/chatBotFAQ';


function App() {
    return (
        <div className="app-container">
            <Navigation />
            <main className="content-container">
                <Routes>
                    <Route path="/" element={<Dashboard />} />
                    <Route path="/diagrams" element={<DiagramPage />} />
                    <Route path="/residents" element={<ResidentsPage />} />
                    <Route path="/residents/add" element={<AddResident />} />
                    <Route path="/residents/edit/:id" element={<EditResident />} />
                    {/* Route 404 */}
                    <Route path="*" element={
                        <div className="not-found">
                            <h2>Page non trouvée</h2>
                            <p>La page que vous recherchez n'existe pas.</p>
                        </div>
                    } />
                    <Route path="/chatBot" element={<ChatbotFAQ />} />
                </Routes>
            </main>
        </div>
    );
}

export default App;
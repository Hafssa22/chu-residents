import React, { useState } from 'react';
import '../styles/chatBotFAQ.css'

const faqData = [
    {
        question: "Comment désactiver un résident automatiquement ?",
        answer: "Les comptes des résidents sont désactivés automatiquement après 4 ans pour les services médicaux, et 5 ans pour les services chirurgicaux."
    },
    {
        question: "Comment générer une fiche PDF d'un résident ?",
        answer: "Rendez-vous sur la page des résidents et cliquez sur 'Générer PDF' à côté du nom du résident."
    },
    {
        question: "Quels sont les KPI affichés sur le tableau de bord ?",
        answer: "Le tableau de bord affiche le nombre total de résidents, la répartition par type de service, et le taux d'inactivité."
    },
    {
        question: "Comment lancer une migration des données ?",
        answer: "Cliquez sur le bouton 'Lancer migration' dans le tableau de bord pour transférer les données depuis l'ancien système vers le nouveau.",
        hasAttention: true,
        attentionMessage: "ATTENTION vous devez faire la migration une seule fois."
    },
    {
        question: "Comment fonctionne le moteur de recherche ?",
        answer: "Vous pouvez chercher un résident par nom, matricule, email, ou service dans la barre de recherche."
    }
];

const ChatBOTFAQ = () => {
    const [activeIndex, setActiveIndex] = useState(null);

    const toggleAnswer = (index) => {
        setActiveIndex(index === activeIndex ? null : index);
    };

    return (
        <div className="assistant-page">
            <div className="chatbot-container">
                <h1 className="chatbot-title">Assistant CHU – FAQ Interactive</h1>

                <div className="faq-container">
                    {faqData.map((faq, index) => (
                        <div
                            key={index}
                            className={`faq-item ${activeIndex === index ? 'active' : ''}`}
                        >
                            <button
                                onClick={() => toggleAnswer(index)}
                                className="faq-question"
                            >
                                {faq.question}
                            </button>

                            {activeIndex === index && (
                                <div className="faq-answer">
                                    <p>{faq.answer}</p>

                                    {faq.hasAttention && (
                                        <div className="attention-message">
                                            {faq.attentionMessage}
                                        </div>
                                    )}
                                </div>
                            )}
                        </div>
                    ))}
                </div>

            </div>
        </div>
    );
};

export default ChatBOTFAQ;
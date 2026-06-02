import React, { useState, useEffect } from 'react';
import AlertStreamView from './views/AlertStreamView';
import IncidentQueueView from './views/IncidentQueueView';
import ResponseConsoleView from './views/ResponseConsoleView';
import MetricsDashboardView from './views/MetricsDashboardView';

// MVC: Root App — manages top-level navigation between views
// PATTERN: MVC (View orchestrator)
// ARCHITECTURE: SOA — each tab communicates with a different SOA service
export default function App() {
  const [activeTab, setActiveTab] = useState('metrics');
  const [connectionStatus, setConnectionStatus] = useState('connecting');

  useEffect(() => {
    // Simulate WebSocket/SSE connection for Observer pattern
    // PATTERN: Observer — dashboard receives push updates
    setTimeout(() => setConnectionStatus('live'), 1500);
  }, []);

  const tabs = [
    { id: 'metrics',   label: 'SOC Overview',     icon: '📊' },
    { id: 'alerts',    label: 'Alert Stream',      icon: '🚨' },
    { id: 'incidents', label: 'Incident Queue',    icon: '🔥' },
    { id: 'response',  label: 'Response Console',  icon: '⚡' },
  ];

  const styles = {
    app: { minHeight: '100vh', background: '#0f1117' },
    header: {
      background: '#1a1d2e', borderBottom: '1px solid #2d3748',
      padding: '0 2rem', display: 'flex', alignItems: 'center',
      justifyContent: 'space-between', height: '60px'
    },
    logo: { display: 'flex', alignItems: 'center', gap: '10px' },
    logoText: { fontSize: '18px', fontWeight: '700', color: '#63b3ed' },
    logoSub: { fontSize: '11px', color: '#718096', marginTop: '2px' },
    statusDot: {
      width: '8px', height: '8px', borderRadius: '50%',
      background: connectionStatus === 'live' ? '#48bb78' : '#ed8936',
      display: 'inline-block', marginRight: '6px',
      animation: connectionStatus === 'live' ? 'pulse 2s infinite' : 'none'
    },
    statusText: { fontSize: '12px', color: connectionStatus === 'live' ? '#48bb78' : '#ed8936' },
    nav: { background: '#1a1d2e', borderBottom: '1px solid #2d3748', display: 'flex', padding: '0 2rem' },
    tab: {
      padding: '14px 20px', cursor: 'pointer', fontSize: '13px',
      fontWeight: '500', border: 'none', background: 'none',
      color: '#718096', borderBottom: '2px solid transparent', transition: 'all 0.2s'
    },
    activeTab: {
      padding: '14px 20px', cursor: 'pointer', fontSize: '13px',
      fontWeight: '600', border: 'none', background: 'none',
      color: '#63b3ed', borderBottom: '2px solid #63b3ed'
    },
    content: { padding: '2rem' },
  };

  return (
    <div style={styles.app}>
      <style>{`
        @keyframes pulse { 0%,100%{opacity:1} 50%{opacity:0.4} }
        button:hover { opacity: 0.85; }
      `}</style>

      <header style={styles.header}>
        <div style={styles.logo}>
          <span style={{ fontSize: '24px' }}>🛡️</span>
          <div>
            <div style={styles.logoText}>SDA-Pro SOC Platform</div>
            <div style={styles.logoSub}>Security Incident Response & Threat Mitigation</div>
          </div>
        </div>
        <div>
          <span style={styles.statusDot}></span>
          <span style={styles.statusText}>
            {connectionStatus === 'live' ? 'Live — Observer connected' : 'Connecting...'}
          </span>
        </div>
      </header>

      <nav style={styles.nav}>
        {tabs.map(tab => (
          <button
            key={tab.id}
            style={activeTab === tab.id ? styles.activeTab : styles.tab}
            onClick={() => setActiveTab(tab.id)}
          >
            {tab.icon} {tab.label}
          </button>
        ))}
      </nav>

      <main style={styles.content}>
        {activeTab === 'metrics'   && <MetricsDashboardView />}
        {activeTab === 'alerts'    && <AlertStreamView />}
        {activeTab === 'incidents' && <IncidentQueueView />}
        {activeTab === 'response'  && <ResponseConsoleView />}
      </main>
    </div>
  );
}

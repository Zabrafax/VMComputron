import { useState } from 'react';
import ConsoleButton from './ConsoleButton';
import styles from './Console.module.css';
import Trash from './icons/Trash.jsx';
import Chevron from './icons/Chevron.jsx';

function ConsoleWindow({msgs=[]}) {
    const [collapsed, setCollapsed] = useState(false);
    const [messages, setMessages] = useState(msgs);

    const toggleCollapsed = () => {
        setCollapsed(prev => !prev);
    };

    const clearMessages = () => {
        setMessages([]);
    }

    return (
        <div className={styles.ConsoleWindow}>
            <div 
                className={`${styles.Header} ${
                    collapsed ? styles.HeaderCollapsed : ''
                }`}
            >
                <ConsoleButton
                    icon={<Chevron direction={collapsed ? 'up' : 'down'} />}
                    onClick={toggleCollapsed}
                />
                <h2 className={styles.Window__title} onClick={toggleCollapsed}>Console</h2>
                <ConsoleButton enabled={messages.length} onClick={clearMessages} icon={<Trash/>} />
            </div>
            <div 
                className={`${styles.Content} ${
                    collapsed ? styles.ContentCollapsed : ''
                }`}
            >
                {messages.map((msg, index) => (
                    <p key={index}>
                        {msg}
                    </p>
                ))}
            </div>
            
        </div>
    )
}

export default ConsoleWindow;
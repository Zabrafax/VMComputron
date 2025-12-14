import { useState } from 'react';
import ConsoleButton from './ConsoleButton';
import styles from './Console.module.css';
import Trash from './icons/Trash.jsx';
import Chevron from './icons/Chevron.jsx';
import SimpleBar from 'simplebar-react';
import { useServerContext } from '../../contexts/ServerContext.jsx';

function ConsoleWindow() {
    const [collapsed, setCollapsed] = useState(false);
    const {consoleLines, clearConsole} = useServerContext();

    const toggleCollapsed = () => {
        setCollapsed(prev => !prev);
    };

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
                <ConsoleButton enabled={consoleLines.length} onClick={clearConsole} icon={<Trash/>} />
            </div>
            <div 
                className={`${styles.Content} ${
                    collapsed ? styles.ContentCollapsed : ''
                }`}
            >
                <SimpleBar 
                    className={`${styles.Content__wrapper} sb-scroll console-scroll`}
                    autoHide={false}
                    scrollbarMaxSize={80}
                >
                    {consoleLines.map((line, index) => (
                        <p key={index}>
                            {line}
                        </p>
                    ))}
                </SimpleBar>
            </div>
            
        </div>
    )
}

export default ConsoleWindow;
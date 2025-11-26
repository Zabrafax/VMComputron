import styles from './App.module.css';
import Header from "./components/Header.jsx";
import MemoryWindow from "./components/memoryWindow/MemoryWindow.jsx";

function App() {
  return (
    <div className={styles.App__wrapper}>
      <Header />

      <div className={styles.Main__content__wrapper}>
        <div className={styles.Left__content__wrapper}>
          <div className={styles.Code__wrapper}>Code wrapper</div>
          <div className={styles.Console__wrapper}>Console wrapper</div>
        </div>
        <div className={styles.Memory__wrapper}>
          <MemoryWindow />
        </div>
      </div>
    </div>
  )
}

export default App

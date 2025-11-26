import styles from "./RegistersBoard.module.css";
import {useState} from "react";

function RegistersBoard({ className, setRegisters, type }) {
  const registersCPU = [
    'green','orange', 'orange', 'orange',
    'green', 'green', 'green',
    'orange', 'orange', 'orange',
    'green', 'green', 'green',
    'orange', 'orange', 'orange'
  ];

  const registersMemory = [
    'red','blue','blue','blue',
    'red','red','red',
    'blue','blue','blue',
    'red','red','red',
    'blue','blue','blue'
  ];

  let registers;
  if (type === 'cpu') {
    registers = registersCPU;
  } else {
    registers = registersMemory;
  }

  const [activeRegisters, setActiveRegisters] = useState([0, 1, 2, 3, 10, 11, 12]);

  function handleActivatingRegister(number) {
    setActiveRegisters(prev =>
      prev.includes(number)
        ? prev.filter(i => i !== number)
        : [...prev, number]
    );
  }

  return (
    <div className={`${styles.RegistersBoard} ${className || ''}`}>
      <div className={styles.Registers__wrapper}>
        {registers.map((register, index) => (
          <button
            key={index}
            className={`
              ${styles.Register}
              ${type === 'cpu' ? (register === 'green' ? styles.Green : styles.Orange) : (register === 'red' ? styles.Red : styles.Blue)}
              ${activeRegisters.includes(index) ? styles.Active : ''}
            `}
            onClick={() => handleActivatingRegister(index)}
          >

          </button>
        ))}
      </div>

      <div className={styles.Counter}>
        <div className={styles.Counter__title__wrapper}>
          <p className={styles.Counter__title}>{type === 'cpu' ? 'CPU' : 'Memory'}</p>
        </div>
        <div className={styles.Counter__window}>
          <p className={styles.Counter__number}>{activeRegisters.length}</p>
        </div>
      </div>
    </div>
  );
}

export default RegistersBoard;
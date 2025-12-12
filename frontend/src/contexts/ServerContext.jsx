import {createContext, useContext, useEffect, useRef, useState} from "react";
import {Client} from "@stomp/stompjs";
import SockJS from "sockjs-client";

const ServerContext = createContext();

export function ServerContextProvider({ children }) {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const client = useRef(null);

  const [PC, setPC] = useState([0, []]);
  const [SP, setSP] = useState([0, []]);
  const [A, setA] = useState([0, []]);
  const [X, setX] = useState([0, []]);
  const [RH, setRH] = useState([0, []]);
  const [RL, setRL] = useState([0, []]);


  useEffect(() => {
    client.current = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      reconnectDelay: 5000,
      onConnect: () => {
        console.log('Connected!');
        client.current.subscribe('/topic/greetings', (msg) => {
          const body = JSON.parse(msg.body);
          setMessages((prev) => [...prev, body.content]);
        });

        client.current.subscribe('/topic/register/PC', (msg) => {
          const data = JSON.parse(msg.body);
          console.log("afwafa" + data);
          setPC([data.newValue, data.cpu]);
        });

        client.current.subscribe('/topic/register/SP', (msg) => {
          const data = JSON.parse(msg.body);
          setSP([data.newValue, data.cpu]);
        });

        client.current.subscribe('/topic/register/A', (msg) => {
          const data = JSON.parse(msg.body);
          setA([data.newValue, data.cpu]);
        });

        client.current.subscribe('/topic/register/X', (msg) => {
          const data = JSON.parse(msg.body);
          setX([data.newValue, data.cpu]);
        });

        client.current.subscribe('/topic/register/RH', (msg) => {
          const data = JSON.parse(msg.body);
          setRH([data.newValue, data.cpu]);
        });

        client.current.subscribe('/topic/register/RL', (msg) => {
          const data = JSON.parse(msg.body);
          setRL([data.newValue, data.cpu]);
        });
      },
      onStompError: (frame) => {
        console.error('Error', frame);
      }
    });

    client.current.activate();

    return () => {
      client.current.deactivate();
    };
  }, []);

  function sendMessage() {
    if (client.current && client.current.connected) {
      client.current.publish({
        destination: '/app/hello',
        body: JSON.stringify(input)
      });
      setInput('');
    }
  }

  function updateRegister(register, value) {
    if (client.current && client.current.connected) {
      console.log("aboba");
      client.current.publish({
        destination: '/app/registerUpdated',
        body: JSON.stringify({
          register: register,
          newValue: value
        }),
      });
    }
  }

  return (
    <ServerContext.Provider value={{
      messages,
      input,
      setInput,
      sendMessage,

      registers: {PC: PC, SP: SP, A: A, X: X, RH: RH, RL: RL},
      updateRegister,
    }}>
      {children}
    </ServerContext.Provider>
  );
}

export const useServerContext = () => useContext(ServerContext);
// task-ui/src/App.tsx
import { useEffect, useState, type FormEvent } from "react";
import { countByStatus, type Task } from "./tasks";

export default function App() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [title, setTitle] = useState("");
  const [error, setError] = useState<string | null>(null);

  async function load() {
    const res = await fetch("/api/tasks");
    if (!res.ok) { setError(`Could not load tasks (${res.status})`); return; }
    setTasks(await res.json());
    setError(null);
  }

  useEffect(() => { void load(); }, []);

  async function add(event: FormEvent) {
    event.preventDefault();
    const res = await fetch("/api/tasks", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ title }),
    });
    if (!res.ok) { setError(`Could not add the task (${res.status})`); return; }
    setTitle("");
    await load();
  }

  const counts = countByStatus(tasks);
  return (
    <main>
      <h1>Task Tracker</h1>
      <form onSubmit={add}>
        <label htmlFor="title">New task</label>
        <input id="title" value={title} onChange={(e) => setTitle(e.target.value)} required maxLength={200} />
        <button type="submit">Add</button>
      </form>
      {error && <p role="alert">{error}</p>}
      <p>{counts.OPEN ?? 0} open, {counts.DONE ?? 0} done</p>
      <ul>{tasks.map((t) => <li key={t.id}>{t.title} <small>{t.status}</small></li>)}</ul>
    </main>
  );
}
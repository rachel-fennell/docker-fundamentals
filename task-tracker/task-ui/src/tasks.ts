// task-ui/src/tasks.ts
export type Task = { id: number; title: string; status: string; dueDate: string | null };

export function countByStatus(tasks: Task[]): Record<string, number> {
  return tasks.reduce<Record<string, number>>((acc, t) => ({ ...acc, [t.status]: (acc[t.status] ?? 0) + 1 }), {});
}


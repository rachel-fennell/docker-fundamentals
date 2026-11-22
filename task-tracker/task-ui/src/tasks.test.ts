// task-ui/src/tasks.test.ts
import { describe, expect, it } from "vitest";
import { countByStatus } from "./tasks";

describe("countByStatus", () => {
  it("counts tasks per status", () => {
    const tasks = [
      { id: 1, title: "a", status: "OPEN", dueDate: null },
      { id: 2, title: "b", status: "DONE", dueDate: null },
      { id: 3, title: "c", status: "OPEN", dueDate: null },
    ];
    expect(countByStatus(tasks)).toEqual({ OPEN: 2, DONE: 1 });
  });
});
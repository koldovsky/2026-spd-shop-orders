#!/usr/bin/env node
// PreToolUse hook (Edit|Write): applied Flyway migrations are immutable.
// Exit 2 blocks the tool call; stderr goes back to Claude as the reason.
// owner: @koldovsky · why: an edited migration broke staging twice · review-by: 2027-02-19
import { appendFileSync, existsSync, readFileSync } from "node:fs";
import { join, relative, resolve } from "node:path";

const input = JSON.parse(readFileSync(0, "utf8") || "{}");
const projectDir = process.env.CLAUDE_PROJECT_DIR || input.cwd || process.cwd();
const filePath = input.tool_input?.file_path;
if (!filePath) process.exit(0);

const rel = relative(projectDir, resolve(projectDir, filePath)).split("\\").join("/");
const isMigration = rel.startsWith("src/main/resources/db/migration/");
const exists = existsSync(resolve(projectDir, filePath));

function log(decision) {
  const line = `${new Date().toISOString()} ${decision} ${input.tool_name} ${rel}\n`;
  try { appendFileSync(join(projectDir, ".claude/hooks/protect-paths.log"), line); } catch {}
}

if (isMigration && exists) {
  log("BLOCK");
  process.stderr.write(
    `Blocked by protect-paths hook: ${rel} is an applied Flyway migration and must not change.\n` +
    `Create a new migration instead, e.g. src/main/resources/db/migration/V<next>__<what>.sql.\n`
  );
  process.exit(2);
}
if (isMigration) log("ALLOW-NEW");
process.exit(0);

# Team AI agreement — orders team

<!-- owner: @koldovsky · review-by: 2027-02-19 · change it by PR, CODEOWNERS approve -->

One page. It says how far agents may go in this repo, how we check their work, and who owns the harness.

## 1. Autonomy matrix

Levels: **L0** no agent · **L1** agent suggests, human writes · **L2** agent writes, human reviews every line ·
**L3** agent writes and runs checks, human reviews the PR · **L4** agent merges when the gates are green, human audits samples.

| Change type | Level | Evidence that earns this level | Who signs off |
|---|---|---|---|
| Tests, test data | L4 | the new tests fail without the change and pass with it; CI green | sampled weekly |
| Endpoint in an existing resource | L3 | MockMvc test per status code; `./gradlew test` green; contract note if the shape changes | any team member |
| Domain rule (limits, statuses) | L3 | unit test per rule incl. the boundary; reviewer subagent verdict | domain owner |
| New Flyway migration | L2 | migration runs on H2 and in compose; Hibernate `validate` passes; rollback note | tech lead |
| Changing an applied migration | L0 | — blocked by the `protect-paths` hook | — |
| Pricing logic | L0 here | lives in shop-pricing only | — |
| Harness files (`AGENTS.md`, `.claude/**`) | L2 | A/B note: same task before and after | harness owners (CODEOWNERS) |

Rule of thumb: an agent may go one level higher on a change type only when the check for it is cheap,
independent of the agent, and runs every time.

## 2. Who owns agent output

The person who opens the PR owns it, as if they wrote it. "The agent did it" is not an answer in review.

## 3. Harness owners

- `AGENTS.md`, `.claude/**`, `docs/ai/**`: @koldovsky (see `.github/CODEOWNERS`).
- Shared skills and hooks come from `2026-spd-shop-harness`; changes go there, not into copies here.

## 4. How rules change

1. Anyone opens a PR that changes a rule, with the reason and an A/B note for behaviour changes.
2. A harness owner reviews it. Every rule carries `owner`, `why` and `review-by` in a header comment.
3. A rule past its `review-by` date is re-confirmed or deleted at the monthly harness review.

## 5. Personal vs team

Personal preferences go to `~/.claude/CLAUDE.md` and `.claude/settings.local.json` (git-ignored).
No `CLAUDE.md` or `CLAUDE.local.md` in the repo: either one would make Claude Code skip `AGENTS.md`.

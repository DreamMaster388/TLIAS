# AGENTS.md

## Overview

Vanilla HTML/CSS/JS front-end for "tlias 智能学习辅助系统" — an employee management UI. No build tools, no package manager, no server.

## Run

Open `tlias.html` in a browser. No server needed.

## Architecture

- `tlias.html` — single-page app with inline JS (mock data, pagination, search UI stubs)
- `css/style.css` — all styles
- Mock data (30 records) hardcoded in `<script>` at bottom of HTML
- Backend CRUD/search endpoints are **not implemented** — buttons show `alert()` stubs
- Search button uses inline mock data; the `click` handler has a `// TODO` placeholder

## Conventions

- Language: Chinese (UI text, comments)
- All state is in-memory (no persistence across reloads)
- No testing, linting, or typechecking infrastructure exists

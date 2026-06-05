# TTM Control OS

A text-based Android command control system.

## Overview

TTM Control OS allows users to control Android device functions using natural text commands.

Example:

open whatsapp  
set brightness 80  
set volume 50  
uninstall instagram  
show notifications  

## Features (V1)

- App control (open/uninstall)
- Device control (brightness, volume)
- Installed app listing
- Notification source tracking
- Policy-based execution engine

## Architecture

Command → Parser → Intent → PolicyEngine → Executor → Android API

## Safety Model

All commands are validated through a policy engine before execution.

System apps are protected by default.

## Tech Stack

- Kotlin
- Android Jetpack Compose
- Android System APIs

## Status

MVP Release (V1)

# Architecture

## Layers

### UI Layer
CommandScreen (Compose)

### Core Layer
- CommandParser
- TTMIntent
- PackageResolver

### Security Layer
- Capability
- PolicyEngine

### Execution Layer
- Executor
- DeviceExecutor

### System Layer
- NotificationTrackingService

## Flow

User Input
  ↓
Parser
  ↓
Intent
  ↓
Policy Engine
  ↓
Executor
  ↓
Android System Action

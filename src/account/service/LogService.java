package account.service;

import account.entities.LogEntry;

import java.util.List;

public interface LogService {
    void save(LogEntry log);

    List<LogEntry> getLogs();
}

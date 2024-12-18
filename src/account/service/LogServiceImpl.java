package account.service;

import account.entities.LogEntry;
import account.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    private LogRepository logRepository;

    @Autowired
    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }


    @Override
    public void save(LogEntry log) {
        this.logRepository.save(log);
    }

    @Override
    public List<LogEntry> getLogs() {
        return this.logRepository.findAll();
    }
}

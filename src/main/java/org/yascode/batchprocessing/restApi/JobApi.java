package org.yascode.batchprocessing.restApi;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yascode.batchprocessing.batch.config.StartJob;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/job")
@RequiredArgsConstructor
public class JobApi {

    private final StartJob startJob;

    @GetMapping("launch")
    public void launchJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        startJob.invokeJob("firstJob", jobParameters);
    }

    @GetMapping("launch2")
    public ResponseEntity<?> launchJob2(HttpServletRequest request) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        String remoteAddr = request.getRemoteAddr();
        startJob.invokeJob("myJob", getJobParameters(remoteAddr));
        return ok("Job successfully completed");
    }

    private JobParameters getJobParameters(String remoteAddr) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .addString("remoteAddr", remoteAddr)
                .toJobParameters();
        return jobParameters;
    }

}

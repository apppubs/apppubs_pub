package com.hingecloud.apppubs.pub.service.impl.compile;

import com.hingecloud.apppubs.pub.model.TTask;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public interface CompileHandler {
    void packageRelease(TTask task) throws IOException;
}

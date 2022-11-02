package org.acme;

import com.redhat.jigawatts.Jigawatts;
import org.jboss.byteman.rule.Rule;
import org.jboss.byteman.rule.helper.Helper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class JigawattsService extends Helper
{
    public JigawattsService(Rule rule) {
        super(rule);
    }

    public void checkpoint()
    {
        try
        {
            long lastCheckpoint = System.currentTimeMillis();
            Files.write(Paths.get("./target/checkpoint.last"), List.of(String.valueOf(lastCheckpoint)), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Jigawatts.saveTheWorld("./target/tmp"  + lastCheckpoint);
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    public void restore()
    {
        try {
            String lastCheckpoint = Files.lines(Paths.get("./target/checkpoint.last")).findAny().get();
            Jigawatts.restoreTheWorld("./target/tmp"  + lastCheckpoint);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

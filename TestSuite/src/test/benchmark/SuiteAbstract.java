package test.benchmark;

import test.AbstractProcessor;

public class SuiteAbstract extends AbstractProcessor implements Runnable {

  protected BenchmarkAbstract[] benchmark;
  public boolean finished = false;

  public SuiteAbstract(final String cat, final String prefix) {
    super(cat, prefix);
  }

  public void execute() {
    finished = false;
    new Thread(this).start();
  }

  public void run() {
    for (int i = 0; i < benchmark.length; i++) {
      benchmark[i].execute();
    }
    finished = true;
  }

}

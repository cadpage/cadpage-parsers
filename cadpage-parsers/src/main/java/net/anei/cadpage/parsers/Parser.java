package net.anei.cadpage.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Wrapper class allowing the Cadpage Parser to be invoked as a Java program
 * 
 * Commmand parameters
 * FLGS=<flags>
 *    Flags can be any combination of
 *    P - Preparse message to remove artifacts inserted by a previous message
 *        service.  Should not be necessary if page is coming directly from 
 *        dispatch CAD software
 *    G - Messages should be parsed as general alerts if not recognized as CAD pages
 *    N - Generate new format (may be incompatible with older clients)
 *    - - Read flags from STDIN
 * FMT=<format code>
 *    format code is the parser format code.  Default or - to read from stdin
 * SUBJ=<message subject>
 *    message subject.  Default or - to read from stdin
 * LOG=<filename>
 *    Specify log file name.  Default no logging
 * 
 * Message text is always from from stdin.  Can be terminated with EOF or a
 * line containing
 * <*EOD*>
 * 
 * Output will be written to stdout terminated by a line containing
 * <*EOD*>
 * If parser fails will return an immediate <*EOD*> tag
 */

public class Parser {
  
  private static final String EOD_MARKER = "<*EOD*>";
  
  private String flags = "G";
  private String fmtCode = "-";
  private String subject = "-";
  
  private PrintStream logStream = null;
  
  private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

  private Parser(String[] args) throws IOException {
    if (args.length == 1 && args[0].equals("-version")) {
      displayVersionInfo();
      return;
    }
    setup(args);
    if (logStream != null) {
      logStream.print("Startup Args:");
      for (String arg : args) {
        logStream.print(' ');
        logStream.print(arg);
      }
      logStream.println();
    }
    while (true) {
      String tFlags = getValue(flags);
      String tFmtCode = getValue(fmtCode);
      String tSubject = getValue(subject);
      String text = getText();
      if (text == null) break;
      
      if (logStream != null) {
        logStream.println(Message.escape(text));
        logStream.println(EOD_MARKER);
        logStream.flush();
      }
      
      Message msg = new Message(tFlags.contains("P"), null, tSubject, text);
      MsgParser parser = null;
      try {
        parser = ManageParsers.getInstance().getParser(tFmtCode);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      if (parser != null) {
        int flags = MsgParser.PARSE_FLG_POSITIVE_ID | MsgParser.PARSE_FLG_SKIP_FILTER;
        boolean compatMode = !tFlags.contains("N");
        if (parser.isPageMsg(msg, flags)) {
          System.out.println(CadpageParser.formatInfo(msg.getInfo(), "\n", true, compatMode));
        }
      }
      System.out.println(EOD_MARKER);
      System.out.flush();
    }
  }

  private void setup(String[] args) {
    for (String arg : args) {
      int pt = arg.indexOf('=');
      if (pt < 0) {
        throw new RuntimeException("Invalid argument - " + arg);
      }
      String key = arg.substring(0,pt).trim();
      String val = arg.substring(pt+1).trim();
      if (key.equals("FLGS")) flags = val;
      else if (key.equals("FMT")) fmtCode = val;
      else if (key.equals("SUBJ")) subject = val;
      else if (key.equals("LOG")) {
        try {
          logStream = new PrintStream(val);
        } catch (IOException ex) {
          throw new RuntimeException("Cannot open log file:" + val, ex);
        }
      }
      else {
        throw new RuntimeException("Invalid argument - " + arg);
      }
    }
  }
  
  private String getValue(String value) throws IOException {
    if (value.equals("-")) {
      value = in.readLine();
      if (EOD_MARKER.equals(value)) {
        throw new IOException("inappropraite EOD marker"); 
      }
      if (logStream != null) logStream.println(Message.escape(value));
    }
    return value;
  }
  
  private String getText() throws IOException {
    StringBuilder sb = new StringBuilder();
    while (true) {
      String line = in.readLine();
      if (line == null || line.equals(EOD_MARKER)) {
        if (line == null && sb.length() == 0) return null;
        return sb.toString();
      }
      if (sb.length() > 0) sb.append('\n');
      sb.append(line);
    }
  }

  private void displayVersionInfo() {
    Package pkg = Package.getPackage("net.anei.cadpage.parsers");
    System.err.println("version: " + pkg.getImplementationVersion());
  }

  public static void main(String[] args) throws IOException {
    new Parser(args);
  }
}

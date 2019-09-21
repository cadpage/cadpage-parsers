package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHAuglaizeCountyBParser extends FieldProgramParser {
  
  public OHAuglaizeCountyBParser() {
    super("AUGLAIZE COUNTY", "OH", 
          "ADD:ADDR! SIG:CODE! DATE:DATETIME! DSC:INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@wapakoneta.net";
  }
  
  private static final Pattern DELIM = Pattern.compile("\n|, (?=DATE:)");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("CAD Page ")) return false;
    data.strCallId = subject.substring(9).trim();
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d");
    return super.getField(name);
  }
}

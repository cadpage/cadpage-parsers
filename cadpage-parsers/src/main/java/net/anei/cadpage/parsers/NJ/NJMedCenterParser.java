package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class NJMedCenterParser extends FieldProgramParser {
  
  public NJMedCenterParser() {
    super("", "NJ", 
          "City:CITY! Addr:ADDR! Blg:APT/D! Apt:APT/D! Cross:X! Problem:CALL! Assign:TIME!");
  }
  
  @Override
  public String getLocName() {
    return  "MedicalCentral, NJ";
  }

  @Override
  public String getFilter() {
    return "cad@rwjuh.edu";
  }
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("(?:Cancelled +)?(\\d{4}-\\d{7}) (RECEIVED: .* AVAIL:(?: \\d\\d:\\d\\d)?) *(.*)");
  private static final Pattern TIMES_DELIM_PTN = Pattern.compile(" +(?=[A-Z]+:)");
  private static final Pattern MARKER = Pattern.compile("([A-Z0-9]+) (\\d{4}-\\d{7}) *");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if(match.matches()) {
      setFieldList("INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strSupp = append(TIMES_DELIM_PTN.matcher(match.group(2)).replaceAll("\n"), "\n", match.group(3));
      return true;
    }

    match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    data.strUnit = match.group(1);
    data.strCallId = match.group(2);
    body = body.substring(match.end());
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "UNIT ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.endsWith(")")) {
      int pt = city.lastIndexOf('(');
      if (pt >= 0) {
        city = city.substring(pt+1, city.length()-1).trim();
      }
    }
    return city;
  }
}

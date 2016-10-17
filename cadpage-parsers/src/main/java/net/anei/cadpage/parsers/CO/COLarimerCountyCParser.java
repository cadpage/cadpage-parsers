package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COLarimerCountyCParser extends FieldProgramParser {
  
  public   COLarimerCountyCParser() {
    super("LARIMER COUNTY", "CO", 
          "CALL ADDR! APT? INFO/CS+");
  }
  
  @Override
  public String getFilter() {
    return "crisppaging@fcgov.com";
  }
  
  private static final Pattern MASTER = Pattern.compile("(.*?)(?: ([A-Z]+/[A-Z]+))? - From [A-Z0-9]+ (\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)");
  private static final Pattern RESPOND_TO_PTN = Pattern.compile("([A-Z ]*)\\bRESPOND TO (.*?)(?: FOR (?:AN? )?)(.*)");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("CAD Page")) return false;
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    body = match.group(1).trim();
    data.strUnit = getOptGroup(match.group(2));
    data.strDate = match.group(3);
    data.strTime = match.group(4);
    
    match = RESPOND_TO_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("SRC ADDR APT CALL");
      data.strSource = match.group(1).trim();
      String addr = match.group(2).trim();
      String call = match.group(3);
      if (call != null) {
        parseAddress(addr, data);
        data.strCall = call.trim();
        return true;
      } else {
        parseAddress(StartType.START_ADDR, addr, data);
        data.strCall = getLeft();
        return (data.strCall.length() > 0);
      }
    }

    String[] flds = body.split(",");
    Result res = parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ, flds[0].trim());
    if (flds.length >= 2 && !res.isValid()) return super.parseFields(flds, data);
    
    setFieldList("CALL ADDR APT INFO");
    res.getData(data);
    if (data.strAddress.length() == 0) return false;
    data.strSupp = res.getLeft();
    for (int j = 1; j<flds.length; j++) {
      data.strSupp = append(data.strSupp, ", ", flds[j].trim());
    }
    return true;
  }
  
  @Override
  public String  getProgram() {
    return super.getProgram() + " UNIT DATE TIME";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new AptField("# *(.*)|(\\d+)", true);
    return super.getField(name);
  }
}

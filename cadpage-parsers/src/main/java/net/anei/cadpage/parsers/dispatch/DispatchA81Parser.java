package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

public class DispatchA81Parser extends FieldProgramParser {

  public DispatchA81Parser(String defCity, String defState) {
    super(defCity, defState,
          "ID DATETIME ADDRCITYST! END");
  }

  private static final Pattern MASTER = Pattern.compile("([A-Z]{3}\\d{10}) (\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d)\\b *(.*)");
  private static final Pattern INFO_MARK_PTN = Pattern.compile(";? \\d\\d?/\\d\\d?/\\d\\d \\d\\d?:\\d\\d:\\d\\d - ");
  private static final Pattern TIMES_MARK_PTN = Pattern.compile(" [-/A-Z0-9]+ - (?:Assign|Enroute|On Scene|Available) \\d\\d?/\\d\\d?/\\d\\d \\d\\d?:\\d\\d:\\d\\d\\b");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT) *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern STATE_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: (\\d{5}))?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    data.strCall = subject.length() > 0 ? subject : "ALERT";

    String[] flds = body.split(" :");
    if (flds.length >= 3) {
      return parseFields(flds, data);
    }

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    setFieldList("ID DATE TIME ADDR APT CITY ST PLACE INFO");
    data.strCallId = match.group(1);
    data.strDate = match.group(2);
    data.strTime = match.group(3);
    body = match.group(4);

    body = stripFieldEnd(body, "{incident_code_description");
    int pt = body.indexOf("{incident_code_description");
    if (pt >= 0) body = body.substring(0,pt).trim();

    match = INFO_MARK_PTN.matcher(body);
    if (match.find()) {
      pt = match.start();
      int spt = match.end();
      while (match.find()) {
        data.strSupp = append(data.strSupp, "\n", body.substring(spt, match.start()).trim());
        spt = match.end();
      }
      data.strSupp = append(data.strSupp, "\n", body.substring(spt).trim());
      body = body.substring(0,pt).trim();
    }

    match = TIMES_MARK_PTN.matcher(body);
    if (match.find()) {
      String times = body.substring(match.start()).trim().replace("; ", "\n");
      data.strSupp = append(times, "\n", data.strSupp);
      body = body.substring(0, match.start()).trim();
    }

    Parser p = new Parser(body);
    parseAddress(p.get(','), data);
    String city = p.get(',');
    match = APT_PTN.matcher(city);
    if (match.matches()) {
      data.strApt = append(data.strApt, "-", match.group(1));
      city = p.get(',');
    }
    match = STATE_ZIP_PTN.matcher(city);
    boolean found = match.matches();
    if (!found) {
      data.strCity = city;
      city = p.get(',');
      match = STATE_ZIP_PTN.matcher(city);
      found = match.matches();
    }
    if (found) {
      data.strState = match.group(1);
      if (data.strCity.isEmpty()) data.strCity = getOptGroup(match.group(2));
      city = p.get(',');
    }
    data.strPlace = city;
    while (!p.isEmpty()) {
      data.strPlace = append(data.strPlace, " - ", p.get(','));
    }
    return true;
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}

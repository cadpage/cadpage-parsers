package net.anei.cadpage.parsers.AL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Lauderdale County, AL (B)
 */
public class ALLauderdaleCountyBParser extends FieldProgramParser {

  public ALLauderdaleCountyBParser() {
    super("LAUDERDALE COUNTY", "AL",
          "Loc:ADDR/SXP! Time:TIME! XSts:X! Typ:CALL!");
  }

  @Override
  public String getFilter() {
    return "noreply@everbridge.net,88911";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (body.startsWith("<!doctype html>")) {
      int pt1 = body.indexOf("<!-- Messages start-->\n");
      if (pt1 >= 0) {
        pt1 += 23;
        int pt2 = body.indexOf("<!-- Messages end-->\n", pt1);
        if (pt2 < 0) return false;
        body = body.substring(pt1, pt2);
      }
    }
    return super.parseHtmlMsg(subject, body, data);
  }

  private static final Pattern TRAIL_HTTP_PTN = Pattern.compile("\\.{3} (https://\\S+)$");
  private static final Pattern SUBJECT_PTN = Pattern.compile("([A-Z]{2,5}\\d{8,10}) TYP:.*");
  private static final Pattern RAPIDOS_PTN = Pattern.compile("(?:Location for )?RapidSOS\\s(?:Latitude: *([-+]?\\d{2,3}\\.\\d{3,}), Longitude: *([-+]?\\d{2,3}\\.\\d{3,})|is not available) *(.*)", Pattern.DOTALL);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.isEmpty()) {
      Matcher match = TRAIL_HTTP_PTN.matcher(body);
      if (!match.find()) return false;
      body = body.substring(0,match.start()).trim();
      data.strInfoURL = match.group(1);

      int pt = body.indexOf('\n');
      if (pt < 0) return false;
      subject = body.substring(0,pt).trim();
      body = body.substring(pt+1).trim();
    }
    do {
      Matcher match = SUBJECT_PTN.matcher(subject);
      if (match.matches()) {
        data.strCallId = match.group(1);
        break;
      }

      if (isPositiveId()) {
        setFieldList("CALL INFO");
        data.msgType = MsgType.GEN_ALERT;
        data.strCall = subject;
        for (String line : body.split("\n")) {
          line = line.trim();
          if (line.startsWith("<a ")) continue;
          data.strSupp = append(data.strSupp, "\n", line);
        }
        return true;
      }

      return false;
    } while (false);

    String info = "";
    int pt = body.indexOf(" | ");
    if (pt >= 0) {
      info = body.substring(pt+3).trim();
      body = body.substring(0,pt).trim();
    }
    body = body.replace("Typ:", " Typ:");
    if (!super.parseMsg(body, data)) return false;

    Matcher match = RAPIDOS_PTN.matcher(info);
    if (match.matches()) {
      String gps1 = match.group(1);
      String gps2 = match.group(2);
      if (gps1 != null) setGPSLoc(gps1+','+gps2, data);
      info = match.group(3);
    } else if (info.startsWith("RapidSOS") || info.startsWith("Location for")) {
      info = "";
    }
    data.strSupp = info;

    return true;
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram() + " GPS INFO URL";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern TIME_PTN = Pattern.compile("\\d\\d?:\\d\\d( [AP]M)?");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");
  private class MyTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      if (match.group(1) == null) {
        data.strTime = field;
      } else {
        setTime(TIME_FMT, field, data);
      }
    }
  }
}

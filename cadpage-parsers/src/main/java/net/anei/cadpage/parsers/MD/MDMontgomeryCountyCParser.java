package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class MDMontgomeryCountyCParser extends FieldProgramParser {

  public MDMontgomeryCountyCParser() {
    super("MONTGOMERY COUNTY", "MD",
          "( Trigger:CODE CAD_Incident_Number:ID! Incident_Type:CALL! Location_Name:PLACE! Location:ADDR! Units_Dispatched:UNIT! " +
          "| CAD_Incident_Number:SKIP! Inc_#:ID! Incident_Type:CALL! Location:PLACE! Location_Name:ADDR! Units_Dispatched:UNIT! " +
          "| ID CALL ADDR! CITY Apt/Suite:APT? LOC:PLACE ( Box_Area:BOX! | Area:MAP! Beat:MAP/L! ) X/Y:GPS? TG/CH:CH? Units:UNIT! " +
          ") END");
  }

  @Override
  public String getFilter() {
    return "noreply@everbridge.net,88911,89361";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    body = stripFieldStart(body, "MONTGOMERY CAD MSG\n");
    if (body.startsWith("<!doctype html>\n")) {
      int pt1 = body.indexOf("CAD MSG:");
      if (pt1 >= 0) {
        int pt2 = body.indexOf("</p>\n", pt1);
        if (pt2 < 0) return false;
        body = body.substring(pt1, pt2).trim();
      } else if ((pt1 = body.indexOf("MESSAGE:")) >= 0) {
        int pt2 = body.indexOf('\n', pt1);
        if (pt2 < 0) return false;
        body = body.substring(pt1, pt2).trim();
      } else if ((pt1 = body.indexOf("Trigger:")) >= 0) {
        int pt2 = body.indexOf("</p>", pt1+8);
        if (pt2 < 0) return false;
        body = body.substring(pt1, pt2).trim().replace("<br>", "\n");
      } else {
        return false;
      }
      return parseMsg(body, data);
    }

    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "");
    if (body.startsWith("CAD MSG:")) {
      body = body.substring(8).trim();
      return parseFields(body.split(" \\* "), data);
    }

    if (body.startsWith("Trigger:") || body.startsWith("CAD Incident Number:")) {
      return parseFields(body.split("\n+"), data);
    }

    if (body.startsWith("MESSAGE:")) {
      setFieldList("INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = body.substring(8).trim();
      return true;
    }
    return false;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("F\\d{10}");
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|SUITE|RM|ROOM|UNIT)[ #]*(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" / ", ",");
      super.parse(field, data);
    }
  }
}

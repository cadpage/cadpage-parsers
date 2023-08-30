package net.anei.cadpage.parsers.MO;


import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Monett, MO
 */

public class MOMonettParser extends FieldProgramParser{

  private static final Pattern ALPHA_CHECK_PTN = Pattern.compile(".*[A-Za-z].*");

  public MOMonettParser() {
    super("MONETT", "MO",
      "Address:ADDRCITY! Category:CALL! SubCategory:CALL! Open:DATETIME! Dispatch:DATETIME! Enroute:SKIP! Arrival:SKIP! Closed:SKIP! NAME+? INFOMAP+");
  }

  public String getFilter() {
    return "monettpd@cityofmonett.com,911EMAMONETTLAWCO@CITYOFMONETT.COM,911MONETTLAWCO@OMNIGO.COM,noreply@omnigo.com";
  }

  protected boolean parseMsg(String subject, String body, Data data) {

    int appendedNotes = body.indexOf("\n****** Appended notes from Work Area");
    if (appendedNotes >= 0) body = body.substring(0, appendedNotes).trim();

    body = body.replace("", "").trim();
    int partCheck = body.indexOf("Address:");
    if (partCheck != body.lastIndexOf("Address:")) {
      String[] parts = body.split("Address:");
      body = "Address:" + parts[1];
      int oldInfo = parts[1].indexOf("Closed:") + 7;
      if (oldInfo - 7 >= 0) {
        parts[1] = parts[1].substring(oldInfo);
        for (int i = 2; i < parts.length; i += 1) {
          parts[i] = parts[i].substring(oldInfo);
          int shortest = Math.min(parts[1].length(), parts[i].length());
          int a = 0;
          for ( ; a < shortest; a++) {
            if (parts[1].charAt(a) != parts[i].charAt(a)) break;
          }
          if (a < shortest) a = parts[i].substring(0,a).lastIndexOf("\n")+1;
          String tmp = parts[i].substring(a).trim();
          body = append(body, "\n", tmp);
        }
      }
    }
    return parseFields(body.split("\n"), 2, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("CALL_RECEIVED")) return new SkipField("Call Received on .*");
    if (name.equals("INFOMAP")) return new MyInfoMapField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {

    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " - ", field);
    }
  }

  private class MyNameField extends NameField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains(",") || !ALPHA_CHECK_PTN.matcher(field).find()) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      data.strName = data.strName.trim();
      if (data.strName.startsWith(",")) data.strName = data.strName.substring(1).trim();
      if (data.strName.endsWith(",")) data.strName = data.strName.substring(0,data.strName.length()-1).trim();
      data.strName = append(data.strName, " / ", field);
    }
  }

  private class MyInfoMapField extends InfoField {

    @Override
    public void parse(String field, Data data) {
      field = cleanWirelessCarrier(field);
      if (field.startsWith("©")) return;
      if (field.startsWith("Call Received on")) {
        data.strPlace = data.strSupp;
        data.strSupp = "";
        return;
      }
      if (field.startsWith("Location:")) {
        data.strMap = field.substring(9).trim();
      } else {
        data.strSupp = append (data.strSupp, " / ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE MAP INFO";
    }
  }
}

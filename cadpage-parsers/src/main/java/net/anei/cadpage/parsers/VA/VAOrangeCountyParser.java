package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class VAOrangeCountyParser extends FieldProgramParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("(\\d{1,2}/\\d{1,2}/\\d{4}) (\\d\\d:\\d\\d)");
  
  public VAOrangeCountyParser() {
    super(CITY_LIST, "ORANGE COUNTY", "VA",
           "[LOCATION]:ADDR/S5XPP! [NATURE]:CALL! [INCIDENT#]:ID BOX:BOX Map:MAP");
  }
  
  @Override
  public String getFilter() {
    return "orange911@oorange.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (subject.length() == 0) return false;
    
    // Sometimes leading square bracket terms do not make it into the subject line
    // So we will try to fix that here.
    while (body.startsWith("[")) {
      int pt = body.indexOf(']');
      if (pt < 0) return false;
      subject = subject + '|' + body.substring(1,pt).trim();
      body = body.substring(pt+1).trim();
    }
    
    // Now that we have built it up, split up the subject line
    String[] subParts = subject.split("\\|");
    
    // Detect and try to fix IAR alterations
    if (subParts.length == 1 && !subject.contains(" ")) {
      data.strSource = subject;
      int pt = body.indexOf("  ");
      if (pt < 0) return false;
      body = "[LOCATION]:" + body.substring(0,pt+2) + "[NATURE]:" + body.substring(pt+2);
    }
    
    // Otherwise process the different subject parts
    else {
      boolean good = false;
      String lastTerm = null;
      for (String part : subParts) {
        part = part.trim();
        if (part.equals("Orange911")) {
          good = true;
          continue;
        }
        Matcher match = SUBJECT_PTN.matcher(part);
        if (match.matches()) {
          data.strDate = match.group(1);
          data.strTime = match.group(2);
          continue;
        }
        lastTerm = part;
      }
      if (!good) return false;
      if (body.startsWith(":")) {
        body = '[' + lastTerm + ']' + body;
      }
    }
    
    // See if this should be a general alert
    if (!body.contains("[INCIDENT#]:") && !body.contains("[NATURE]:")) {
      return data.parseGeneralAlert(this, body);
    }
      
    body = body.replace(" BOX ", "  BOX: ").replace(" Map ", "  Map: ").replace("[", " [");
    body = body.trim();
    if (!super.parseMsg(body, data)) return false;
    data.strPlace = data.strPlace.replace(" Village of", "");
    data.strAddress = data.strAddress.replace("DAILY DR", "DAILEY DR");  // Dispatch typo
    return true;
  }
  
  @Override
  public String getProgram() {
    return "DATE TIME SRC " + super.getProgram() + " PLACE";
  }
  
  private class MyIdField extends IdField {
    
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCallId = p.get(' ');
      
      // The time field here appears to be an est incident time.  Which we
      // will use if we didn't get a dispatch time from the subject
      String sTime = p.get(' ');
      if (data.strTime.length() == 0) data.strTime = sTime;
      
      field = p.get();
      parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY, field, data);
      data.strCross = getStart();
      parseInfo(getLeft(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "ID X CITY INFO";
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      super.parse(p.get("  "), data);
      parseInfo(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL INFO";
    }
  }
  
  private class MyBoxField extends BoxField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }

  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Page ")) {
        field = field.substring(5).trim();
      } else {
        if ("Page ".startsWith(field)) return;
      }
      Parser p = new Parser(field);
      super.parse(p.get(' '), data);
      String id = p.get(' ');
      if (data.strCallId.length() == 0) data.strCallId = id;
      String time = p.get();
      if (data.strTime.length() == 0) data.strTime = time;
    }
    
    @Override
    public String getFieldNames() {
      return "MAP ID TIME";
    }
  }
  
  private static void parseInfo(String info, Data data) {
    int pt = info.indexOf(" E911 Info ");
    if (pt >= 0) info = info.substring(0,pt).trim();
    data.strSupp = append(data.strSupp, "  ", info);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("BOX")) return new MyBoxField();
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }
  
  private static final String[] CITY_LIST = new String[]{
    "GORDONSVILLE",
    "ORANGE",
    "BARBOURSVILLE",
    "LOCUST GROVE",
    
    "MADISON COUNTY",
    "CULPEPER COUNTY",
    "SPOTSYLVANIA COUNTY",
    "LOUISA COUNTY",
    "ALBEMARLE COUNTY",
    "GREENE COUNTY"
  };
}

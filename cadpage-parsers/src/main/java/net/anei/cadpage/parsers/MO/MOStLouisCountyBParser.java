package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class MOStLouisCountyBParser extends FieldProgramParser {
  
  private static final Pattern TIME_PTN = Pattern.compile(" +(\\d\\d:\\d\\d)$");
  private static final Pattern CARD_PTN = Pattern.compile(",? +([A-Z]{2} +Card ?#(?: ?\\d+\\b)?),?", Pattern.CASE_INSENSITIVE);
  
  public MOStLouisCountyBParser() {
    super("ST LOUIS COUNTY", "MO",
          "ADDR/SLU! Map:MAP X-St:X");
    setupCallList(CALL_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "bpage@scfa911.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals(".SCFA Page")) return false;
    Matcher match = TIME_PTN.matcher(body);
    if (!match.find()) return false;
    data.strTime = match.group(1);
    body = body.substring(0,match.start());
    
    StringBuffer sb = new StringBuffer();
    match = CARD_PTN.matcher(body);
    while (match.find()) {
      String card = match.group(1);
      if (!card.endsWith("#") && data.strSupp.length() == 0) data.strSupp = card;
      match.appendReplacement(sb, "");
    }
    match.appendTail(sb);
    body = sb.toString();
    
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " INFO TIME";
  }
  
  private static final Pattern UNIT_PATTERN = Pattern.compile(" +(\\d{4}\\b.*?)$");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String sApt = null;
      int flags = 0;
      int pt = field.indexOf(',');
      if (pt >= 0) {
        flags |= FLAG_ANCHOR_END;
        String left = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim(); 
        if (left.startsWith("Apt. ")) {
          left = left.substring(5).trim();
          Parser p = new Parser(left);
          sApt = p.get(' ');
          if (sApt.equals("APT")) sApt = p.get(' ');
          else if (sApt.equals("FL") || sApt.equals("BLD")) {
            sApt = sApt + ' ' + p.get(' ');
          }
          data.strUnit = p.get();
        }
      }
      
      // Avenue H is a problem
      field = field.replace("AVENUE H", "AVENUE-H").replace("AVE H", "AVE-H");
      parseAddress(StartType.START_CALL_PLACE, flags | FLAG_NO_IMPLIED_APT, field, data);
      if (data.strUnit.length() == 0) {
        if (data.strUnit.startsWith(data.strAddress)) data.strUnit = data.strUnit.substring(data.strAddress.length()).trim();
        data.strUnit = getLeft();
      }
      
      if (data.strUnit.length() == 0) {
        Matcher match = UNIT_PATTERN.matcher(data.strAddress);
        if (match.find() && match.start() > 0) {
          data.strUnit = match.group(1);
          data.strAddress = data.strAddress.substring(0,match.start());
        }
      } 
      
      else {
        if (data.strUnit.startsWith(data.strAddress)) {
          data.strUnit = data.strUnit.substring(data.strAddress.length()).trim();
        }
        if (data.strUnit.startsWith("[")) {
          pt = data.strUnit.indexOf(']');
          if (pt >= 0) {
            data.strPlace = append(data.strPlace, " - ", data.strUnit.substring(1,pt).trim());
            data.strUnit = data.strUnit.substring(pt+1).trim();
          }
        }
      }
      
      if (sApt != null) {
        data.strApt = append(data.strApt, "-", sApt);
      }
      
      data.strAddress = data.strAddress.replace("AVENUE-H", "AVENUE H").replace("AVE-H", "AVE H");
    }
  }
  
  private class MyBoxField extends BoxField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strBox)) return;
      data.strBox = append(data.strBox, ",", field);
    }
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("BOX")) return new MyBoxField();
    return super.getField(name);
  }
  
  private static final CodeSet CALL_TABLE = new CodeSet(
      "ABDOMINAL PAIN",
      "ACCIDENTAL INJURY",
      "ALLERGIC REACTION",
      "ASSAULT",
      "ASTEMS",
      "ASTFIR",
      "CHEST PAIN",
      "COMALM",
      "COMERCIAL FIRE",
      "COMMERCIAL FIRE",
      "DIABETIC",
      "DIFFICULTY BREATHING",
      "FAINTING (NEAR FAINTING)",
      "FALL",
      "FULL ARREST",
      "GASOUT",
      "MEDALM",
      "OBS",
      "PERSON DOWN",
      "RESALM",
      "SEIZURE",
      "SICK CASE",
      "STROKE",
      "UNCONSCIOUS PERSON",
      "VEHACC"
      );
  
}

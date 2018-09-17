package net.anei.cadpage.parsers.CA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class CAStocktonBParser extends FieldProgramParser {
  
  private Map<String,Field> subfieldMap;
  private Pattern subfieldKeyPtn;
  private List<Field> subfieldList = new ArrayList<Field>(); 
  
  public CAStocktonBParser() {
    super("STOCKTON", "CA", 
          "TYPE:CALL! LOCATION:ADDR! Received:DATETIME! CFS:ID! APT:APT? Cross_Streets:X? APPARATUS:UNIT! Lat/Long:GPS! INFO+ ");
    subfieldMap = buildSubfieldMap();
    subfieldKeyPtn = buildSubfieldKeyPtn(subfieldMap);
  }

  @Override
  public String getFilter() {
    return "stockadm@ci.stockton.ca.us";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final Pattern MARKER = Pattern.compile("CAD Page for CFS [-0-9]+\\b:? *");
  private static final Pattern MISSING_BRK_PTN = Pattern.compile(" (?=(?:LOCATION|Received|CFS|Cross Streets|APPARATUS|Lat/Long|Notes) *:)");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    do {
      if (MARKER.matcher(subject).matches()) break;
  
      Matcher match = MARKER.matcher(body);
      if (match.lookingAt()) {
        body = body.substring(match.end());
        body = MISSING_BRK_PTN.matcher(body).replaceAll("\n");
        break;
      }
      
      return false;
    } while (false);
    
    subfieldList.clear();
    return parseFields(body.split("\n"), data);

  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('*', '/');
      super.parse(field, data);
    }
  }
  
  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\[\\d{1,2}\\] *");
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Notes:");
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      match = subfieldKeyPtn.matcher(field);
      String key = "INFO";
      int lastPt = 0;
      while (match.find()) {
        String part = field.substring(lastPt, match.start()).trim();
        parseSubfield(key, part, data);
        key = match.group(1);
        lastPt = match.end();
      }
      String part = field.substring(lastPt).trim();
      parseSubfield(key, part, data);
    }

    private void parseSubfield(String key, String part, Data data) {
      if (part.length() == 0) return;
      Field fld = subfieldMap.get(key);
      if (fld != null) {
        fld.parse(part, data);
        subfieldList.add(fld);
      }
    }
    
    @Override
    public String getFieldNames() {
      StringBuilder sb = new StringBuilder();
      for (Field fld : subfieldList) {
        if (sb.length() > 0) sb.append(' ');
        sb.append(fld.getFieldNames());
      }
      return sb.toString();
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\[ProQA: Case Entry Complete:\\]|\\[ProQA Script\\]|int: *\\d+");
  
  /**
   * Build the info subfield processor map
   * @return the map
   */
  private Map<String,Field> buildSubfieldMap() {
    
    Map<String,Field> map = new HashMap<String,Field>();

    map.put("Incident located in", new MapField(){
      @Override
      public void parse(String field, Data data) {
        field = stripFieldStart(field, "DISTRICT ");
        field= stripFieldEnd(field, ".");
        data.strMap = field;
      }
    });
    
    map.put("Incident Number", null);
    map.put("Incident Type", null);
    map.put("Channel", new ChannelField());
    map.put("Caller Name/Location", new PlaceField());
    map.put("PSAP Caller Source", null);
    
    map.put("Location", null);
    map.put("Caller Lat/Lon", null);
    map.put("Callback", new PhoneField());
    map.put("Problem Description", new InfoField(){
      @Override
      public void parse(String field, Data data) {
        data.strSupp = append(data.strSupp, "\n", field);
      }
    });
    
    map.put("Dispatch code", new CodeField(){
      @Override
      public void parse(String field, Data data) {
        int pt = field.indexOf(' ');
        if (pt >= 0) field = field.substring(0,pt);
        data.strCode = field;
      }
    });
    
    map.put("-Comments", new InfoField(){
      @Override
      public void parse(String field, Data data) {
        field = stripFieldEnd(field, "-");
        data.strSupp = append(data.strSupp, "\n", field);
      }
    });
    
    map.put("Successfully paged", null);
    
    map.put("INFO", new InfoField(){
      @Override
      public void parse(String field, Data data) {
        if (INFO_JUNK_PTN.matcher(field).matches()) return;
        data.strSupp = append(data.strSupp, "\n", field);
      }
    });
    
    return map;
  }
  
  /**
   * Build the pattern 
   * @param map info subfield processing map
   * @return info subfield key pattern
   */
  private Pattern buildSubfieldKeyPtn(Map<String, Field> map) {
    StringBuilder sb = new StringBuilder("\\b(");
    boolean first = true;
    for (String key : map.keySet()) {
      if (!first) sb.append('|');
      else first = false;
      sb.append(key);
    }
    sb.append(") *:");
    return Pattern.compile(sb.toString());
  }
}

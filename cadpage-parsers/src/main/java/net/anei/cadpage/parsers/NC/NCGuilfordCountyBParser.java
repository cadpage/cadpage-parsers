package net.anei.cadpage.parsers.NC;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class NCGuilfordCountyBParser extends DispatchSPKParser {
  
  public NCGuilfordCountyBParser() {
    super("GUILFORD COUNTY", "NC");
  }
  
  @Override
  public String getFilter() {
    return "interactcad@gmail.com";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (data.strDate.equals("1/1/77")) data.strDate = data.strTime = "";
    return true;
  }
  
  // The scramble a lot of fields that have to be fixed
  
  private static final Pattern KEYWORD_PTN = Pattern.compile("Location|Location and POI Information|L/L|Cross Street|Areas");
  
  @Override
  public boolean parseFields(String[] flds, Data data) {
    List<String> newFlds = new ArrayList<String>();
    if (!flds[0].startsWith("As of ")) {
      newFlds.add("As of 1/1/77 00:00:00");
    }
    
    boolean append = false;
    boolean unitInfo = false;
    for (String field : flds) {
      
      if (field.equals("Unit Information")) {
        unitInfo = true;
        newFlds.add(field + ':');
        continue;
      }
      
      if (field.startsWith("This message was automatically")) break;
      
      if (!unitInfo) {
        
        boolean keyword = KEYWORD_PTN.matcher(field).matches();
        if (keyword || field.startsWith("<|")) append = false;
        if (append) {
          append = false;
          int ndx = newFlds.size()-1;
          String fld = newFlds.get(ndx) + ':' + field;
          newFlds.set(ndx, fld);
        }
        else {
          if (field.equals("Unit Information")) field += ':';
          newFlds.add(field);
          append = keyword;
        }
      }
      
      else {
        if (field.equals("<|table|>") || field.equals("<|/table|>")) continue;
        if (field.equals("Unit")) continue;
        if (field.equals("Org") || field.equals("Area") || field.equals("Types")) {
          newFlds.remove(newFlds.size()-1);
          continue;
        }
        newFlds.add(field);
      }
    }
    
    flds = newFlds.toArray(new String[0]);
    return super.parseFields(flds, data);
  }
  
}

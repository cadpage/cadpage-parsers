package net.anei.cadpage.parsers.MA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MABarnstableCountyParser extends FieldProgramParser {

  public MABarnstableCountyParser() {
    super("BARNSTABLE COUNTY", "MA", 
          "SRC! CALL! D:MAP! Loc:ADDRCITY! Notes:INFO+");
  }
  
  private static Pattern START_TRASH_PTN = Pattern.compile("^[[^\\p{ASCII}][\\s]]+");
  private static Pattern DELIM = Pattern.compile("[\\s\n]?\n");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    //check subject
    if (!subject.equals("!")) return false;
    body = START_TRASH_PTN.matcher(body).replaceFirst("");
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private static Pattern LOC_FORMAT = Pattern.compile("([^\\(\\)]*?)(?: *\\((?! )([^\\(\\)]*?)\\))?(?: *\\( ([^\\(\\)]*?)\\))?");
  private static Pattern AT_FORMAT = Pattern.compile("@([^\\(\\)]*?)  \\(([^\\(\\)]*?)\\), ([ A-Z]+?)  \\( ([^\\(\\)]*?)\\)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("@")) {
        Matcher mat = AT_FORMAT.matcher(field);
        if (mat.matches()) {
          data.strPlace = mat.group(1).trim();
          parseAddress(mat.group(2), data);
          data.strCity = mat.group(3).trim();
          //sometimes the place in parens at the end is redundant, so check
          String g4 = mat.group(4).trim(); 
          if (g4.length() > 0 && !data.strPlace.endsWith(g4)) {
            if (data.strPlace.length() == 0) data.strPlace = g4;
            else data.strPlace = data.strPlace + " (" + g4 + ")"; 
          }
        } else {
          field = field.substring(1).trim();
          int pt = field.lastIndexOf(';');
          if (pt >= 0) {
            parseAddress(field.substring(0,pt).trim(), data);
            data.strCity = field.substring(pt+1).trim();
          } else {
            super.parse(field, data);
          }
        }
      } else {
        Matcher mat = LOC_FORMAT.matcher(field);
        if (!mat.matches()) abort();
        super.parse(mat.group(1).trim(), data);
        data.strCross = getOptGroup(mat.group(2));
        data.strPlace = getOptGroup(mat.group(3));
        //sometimes there is a near:PLACE construct that goes to the end of the field
        int ni = data.strCross.indexOf("; Near:");
        if (ni != -1) {
          data.strPlace = append(data.strPlace, "; ", data.strCross.substring(ni+2)).trim();
          data.strCross = data.strCross.substring(0, ni).trim();
        }
        //remove leading "/ " and trailing " /" from cross if present
        data.strCross = stripFieldStart(data.strCross, "/");
        data.strCross = stripFieldEnd(data.strCross, "/");
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY X PLACE";
    }
  }
}

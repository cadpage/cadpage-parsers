package net.anei.cadpage.parsers.DE;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class DENewCastleCountyAParser extends FieldProgramParser {
  
  private static final Pattern[] MARKERS = new Pattern[]{
    Pattern.compile("^(?:([0-9A-Z]+) / \\[\\w*FB\\] )?F00 \\d\\d:\\d\\d 1 - (?=T:)"),
    Pattern.compile("^\\d\\d:\\d\\d(?=T:)")
  };
  
  private static final Pattern NON_ASCII_PTN = Pattern.compile("[^\\p{ASCII}]");
  private static final Pattern NAKED_BTWN = Pattern.compile("(?<!X: ?)\\bbtwn\\b");
  
  public DENewCastleCountyAParser() {
    super("NEW CASTLE COUNTY", "DE",
           "T:CALL! L:ADDR! X:X DESC:INFO");
  }
  
  @Override
  public String getFilter() {
    return "@c-msg.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    boolean found = false;
    for (Pattern ptn : MARKERS) {
      Matcher match = ptn.matcher(body);
      if (match.find()) {
        found = true;
        if (match.groupCount() >= 1) data.strSource = getOptGroup(match.group(1));
        body = body.substring(match.end());
        break;
      }
    }
    if (!found) return false;
    
    // If we stripped the source from the front of the field (terminated by " / ")
    // Search for and remove that sequence if it appears anywhere else in the 
    // text body, assuming it was a lead for a second message.
    if (data.strSource.length() > 0) {
      body = body.replace(data.strSource + " / ", "");
    }
    
    if (subject.length() > 0) {
      String[] subjects = subject.split("\\|");
      if (! subjects[subjects.length-1].endsWith("FB")) return false;
      if (subjects.length == 2) data.strSource = subjects[0];
    }
    
    body = NON_ASCII_PTN.matcher(body).replaceAll("");
    body = body.replace('\n', ' ');
    body = body.replaceAll("  +", " ");
    body = body.replace(" :", " DESC:");
    body = NAKED_BTWN.matcher(body).replaceFirst(" X:btwn");
    return super.parseMsg(body, data);
  }
  
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String fld, Data data) {
      Parser p = new Parser(fld);
      String sAddress = p.get(',').replaceAll("~","&");
      parseAddress(sAddress, data);
      p.get(' ');
      fld = p.get();
      parsePlace(fld, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY PLACE";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String fld, Data data) {
      if (fld.startsWith("btwn ")) fld = fld.substring(5).trim();
      Parser p = new Parser(fld);
      super.parse(p.get(" *").replace('~', '&'), data);
      parsePlace(p.get(), data);
    }
  }

  private static final Pattern PLACE_PTN = Pattern.compile("[- \\*]*(.*?)[- \\*]*");
  private void parsePlace(String fld, Data data) {
    Matcher match = PLACE_PTN.matcher(fld);
    if (match.matches()) fld = match.group(1);
    if (fld.length() > 0) {
      if (CITY_SET.contains(fld.toUpperCase())) {
        data.strCity = fld;
      } else {
        data.strPlace = append(data.strPlace, " / ", fld);
      }
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  private static Set<String> CITY_SET = new HashSet<String>(Arrays.asList(new String[]{
      "ARDENCROFT",
      "ARDENTOWN",
      "BELLEFONTE",
      "CLAYTON",
      "DELAWARE CITY",
      "ELSMERE",
      "MIDDLETOWN",
      "NEW CASTLE",
      "NEWARK",
      "NEWPORT",
      "ODESSA",
      "SMYRNA",
      "TOWNSEND",
      "WILMINGTON",
      "BEAR",
      "BROOKSIDE",
      "CLAYMONT",
      "COLLINS PARK",
      "CHRISTIANA",
      "EDGEMOOR",
      "GLASGOW",
      "GREENVILLE",
      "HOCKESSIN",
      "HOLLY OAK",
      "MARSHALLTON",
      "MINQUADALE",
      "MONTCHANIN",
      "NORTH STAR",
      "OGLETOWN",
      "PIKE CREEK",
      "ROCKLAND",
      "ST. GEORGES",
      "STANTON",
      "WILMINGTON MANOR",
      "WINTERTHUR",
      "WINTERSET"
  }));
}



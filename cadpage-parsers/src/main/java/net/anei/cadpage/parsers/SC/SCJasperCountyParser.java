package net.anei.cadpage.parsers.SC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

/**
 * Jasper County, SC
 */
public class SCJasperCountyParser extends DispatchA19Parser {

  public SCJasperCountyParser() {
    super(CITY_CODES, "JASPER COUNTY", "SC");
  }

  @Override
  public String getFilter() {
    return "@jasperscountysc.gov";
  }

  private static final Pattern TAC_PTN = Pattern.compile("(?:TAC|CAD) *\\d.*", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (TAC_PTN.matcher(data.strPlace).matches()) {
      data.strChannel = data.strPlace;
      data.strPlace = "";
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("PLACE", "PLACE CH");
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
     "BEU", "BEAUFORT",
     "BLF", "BLUFFTON",
     "BRN", "BRUNSON",
     "CLY", "CLYO",
     "COS", "COOSAWHATCHIE",
     "CTG", "COTTAGEVILLE",
     "DI" , "DAUFUSKI ISLAND",
     "EBR", "EARLY BRANCH",
     "EDI", "EDISTO ISLAND",
     "EST", "ESTILL",
     "FUR", "FURMAN",
     "GAR", "GARNETT",
     "GIF", "GIFFORD",
     "GP",  "GREEN POND",
     "GUY", "GUYTON",
     "HAM", "HAMPTON",
     "HHI", "HILTON HEAD ISLAND",
     "HVL", "HARDEEVILLE",
     "ISL", "ISLANDTON",
     "JCK", "JACKSONBORO",
     "LGE", "LODGE",
     "LUR", "LURAY",
     "OKA", "OKATIE",
     "PI",  "PARIS ISLAND",
     "PNE", "PINELAND",
     "POO", "POOLER",
     "PR",  "PORT ROYAL",
     "PWT", "PORT WENTWORTH",
     "RDG", "RIDGELAND",
     "RFF", "RUFFIN",
     "RIN", "RINCO",
     "RND", "ROUND O",
     "SAV", "SAVANNAH",
     "SCO", "SCOTIA",
     "SEA", "SEABROOK",
     "SHI", "ST HELENA ISLAND",
     "SHL", "SHELDON",
     "SMO", "SMOAKS",
     "SPR", "SPRINGFIELD",
     "TBI", "TYBEE ISLAND",
     "TLL", "TILLMAN",
     "VRN", "VARNVILLE",
     "WLT", "WALTERBORO",
     "YMS", "YEMASSEE"

  });
}

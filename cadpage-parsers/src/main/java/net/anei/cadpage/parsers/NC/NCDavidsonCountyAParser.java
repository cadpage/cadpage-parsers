package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCDavidsonCountyAParser extends NCGuilfordCountyAParser {

  public NCDavidsonCountyAParser() {
    super("DAVIDSON COUNTY", "NC");
    removeWords("-");
  }

  @Override
  public String getFilter() {
    return "cad@davidsoncountync.gov";
  }

  // This all gets a bit complicated
  // NCDavidsonCountyA and NCGuilfordCounty are adjacent counties with nearly
  // identical formats that are impossible to tell apart.  So they have been
  // merged into one NCGuildfordCounty parser.

  // NCDavidsonCountyA and NCRowanCounty are also adjacent counties with similar
  // formats that we make an extra special effort to make sure do not accept
  // each other page formats

  // But NCRowanCounty and NCGuifordCounty are not adjacent, and keeping their
  // page formats mutually exclusive has proven to be impossible, largely because
  // they both use the ROCK city code to mean different things.

  // Tentative solution, NCDavidsonCountyA is an alias of NCGuildforCounty with
  // some extra logic to drop NCRowanCounty pages.  When NCDavidsonCounty and
  // NCGuilfordCounty are grouped together, NCDavidsonCounty takes over because
  // it is (fortunately) first alpahbetically and this logic is not executed.
  // When Davidson and Rowan Counties are merged togother, it does take effect.

  private static final Pattern BAD_MSG_PTN = Pattern.compile("; *ROCK(?: *;|$)|^CAD:PAGE / ");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (BAD_MSG_PTN.matcher(body).find()) return false;
    return super.parseMsg(subject, body, data);
  }


}

package symbolics.division.occmy.client.view;

import symbolics.division.occmy.client.OCCMYClient;

public class CGroundingView {
    public static void resetAll(boolean extant) {
        CBestView.reset();
        CExteriorityView.reset();
        CInversionView.reset();
        CTreacherousView.reset();
        CNullView.reset();
        if (extant) OCCMYClient.AFFAIRS.enableFor(Perspectives.OBSCURED, 20);
    }
}

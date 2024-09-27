package com.example.assetexdemo1;

import android.os.Bundle;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnboardingTermsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnboardingTermsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView fOTtextView;
    ImageButton fOTbackButton;

    public OnboardingTermsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnboardingTermsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OnboardingTermsFragment newInstance(String param1, String param2) {
        OnboardingTermsFragment fragment = new OnboardingTermsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding_terms, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (this.getView() != null) {
            fOTtextView = getView().findViewById(R.id.fOTtextView);
            fOTbackButton = getView().findViewById(R.id.fOTbackButton);

            String htmlString = "<ol>\n" +
                    "<strong>Introduction</strong><br><br>Welcome to Asset Exchange. By using our app, you agree to comply with and be bound by the following terms and conditions. Please read them carefully. If you do not agree with these terms, you should not use our app.<br><br>" +
                    "<p><strong>Use of the App</strong>  </p>\n" +
                    "<ul>\n" +
                    "<li>&nbsp;<strong>Eligibility</strong>: You must be at least 18 years old to use Asset Exchange. By using the app, you confirm that you meet this age requirement.  </li>\n" +
                    "<li>&nbsp;<strong>Account Responsibility</strong>: You are responsible for maintaining the confidentiality of your account credentials and for all activities that occur under your account. Notify us immediately of any unauthorized use of your account.  </li>\n" +
                    "</ul><br>\n" +
                    "<p><strong>User Content</strong>  </p>\n" +
                    "<ul>\n" +
                    "<li><strong>Ownership</strong>: You retain ownership of all content you upload or create using Asset Exchange. However, by using the app, you grant us a worldwide, royalty-free, and non-exclusive license to use, reproduce, and display your content for the purpose of operating and improving the app.  </li>\n" +
                    "<li><strong>Responsibility</strong>: You are solely responsible for the content you upload and must ensure it does not infringe on the rights of third parties or violate any laws.  </li>\n" +
                    "</ul>\n" +
                    "<p><strong>Access and Permissions</strong></p>\n" +
                    "<ul>\n" +
                    "<li><strong>Portfolio Access</strong>: You can use folders as portfolios and share them with others. You are responsible for managing the access permissions you assign, including view, comment, or approve.  </li>\n" +
                    "<li><strong>Content Sharing</strong>: Any content you choose to share with others is at your own risk. We are not liable for any unauthorized access or misuse of your content.  </li>\n" +
                    "</ul>\n" +
                    "</li>\n" +
                    "<li><p><strong>Version Control</strong></p>\n" +
                    "<ul>\n" +
                    "<li><strong>Reversion</strong>: The app allows reverting to the previous version of an asset. This feature is limited to one version at a time. We are not responsible for any loss of data or assets resulting from version control actions.  </li>\n" +
                    "</ul>\n" +
                    "</li>\n" +
                    "<li><p><strong>Limitations</strong>  </p>\n" +
                    "<ul>\n" +
                    "<li><strong>Paid Version</strong>: The first version of Asset Exchange does not include a paid version or advanced features beyond those specified.  </li>\n" +
                    "<li><strong>Chat Functionality</strong>: Chat functionality is not implemented in the first version. The app currently uses a comment feature for communication.  </li>\n" +
                    "</ul>\n" +
                    "</li>\n" +
                    "<li><p><strong>Limitation of Liability</strong>  </p>\n" +
                    "<ul>\n" +
                    "<li><strong>Disclaimer</strong>: Asset Exchange is provided “as is” and we make no warranties, express or implied, regarding the app’s performance or availability. We are not responsible for any damages arising from your use of the app, including but not limited to data loss, business interruption, or any indirect, incidental, or consequential damages. </li>\n" +
                    "<li><strong>Indemnity</strong>: You agree to indemnify and hold us harmless from any claims, losses, or damages arising from your use of the app or violation of these terms.  </li>\n" +
                    "</ul>\n" +
                    "</li>\n" +
                    "<li><p><strong>Changes to Terms</strong> <br><br>We may update these terms from time to time. Any changes will be posted on this page, and your continued use of the app signifies your acceptance of the updated terms.  </p>\n" +
                    "</li>\n" +
                    "<li><p><strong>Termination</strong> <br><br>We reserve the right to suspend or terminate your access to the app if you violate these terms or if we believe, in our sole discretion, that such action is necessary.  </p>\n" +
                    "</li>\n" +
                    "<li><p><strong>Governing Law</strong><br><br>These terms are governed by and construed in accordance with the laws of [Your Country/State], without regard to its conflict of law principles.  </p>\n" +
                    "</li>\n" +
                    "<li><p><strong>Contact Us</strong> <br><br>If you have any questions about these terms, please contact us at: [AssetEx@gmail.com]</p>\n" +
                    "</li>\n" +
                    "</ol>";

            Spanned spanned = HtmlCompat.fromHtml(htmlString, HtmlCompat.FROM_HTML_MODE_COMPACT);

            fOTtextView.setText(spanned);

            NavController navController = Navigation.findNavController(getActivity().findViewById(R.id.activity_onboarding_nav));

            fOTbackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navController.popBackStack();
                }
            });
        }
    }
}
package com.example.sara.uppgift721;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A fragment for making Toasts.
 */
public class ToastFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static String NAME = "Toast";

    /**
     * These fragments should be instantiated via factory method, not via this constructor!
     */
    public ToastFragment() {
    }

    /**
     * Factory method for making new ToastFragments.
     * @param sectionNumber The number this fragment has in the activity.
     * @return a newly created ToastFragment
     */
    public static ToastFragment newInstance(int sectionNumber) {
        ToastFragment fragment = new ToastFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create a new view for the fragment.
     * @param inflater Inflater to use when inflating new views.
     * @param container The viewGroup in which the Fragment sits.
     * @param savedInstanceState Old bundle for previous instances of the fragment.
     * @return An inflated View object showing the Fragment.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_toast,container,false);
        final Button button = (Button) rootView.findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createToast();
            }
        });


        return rootView;
    }

    /**
     * Create a toast.
     */
    public void createToast() {
        Toast.makeText(getContext(), R.string.toast_text, Toast.LENGTH_SHORT).show();
    }
}

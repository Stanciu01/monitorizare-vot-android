package ro.code4.monitorizarevot.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ro.code4.monitorizarevot.BaseFragment;
import ro.code4.monitorizarevot.R;
import ro.code4.monitorizarevot.db.Data;
import ro.code4.monitorizarevot.db.Preferences;
import ro.code4.monitorizarevot.net.model.Form;
import ro.code4.monitorizarevot.net.model.Version;
import ro.code4.monitorizarevot.util.FormRenderer;
import ro.code4.monitorizarevot.viewmodel.FormsListViewModel;
import ro.code4.monitorizarevot.widget.ChangeBranchBarLayout;
import ro.code4.monitorizarevot.widget.FormSelectorCard;

import java.util.List;

import static ro.code4.monitorizarevot.ToolbarActivity.BRANCH_SELECTION_BACKSTACK_INDEX;

public class FormsListFragment extends BaseFragment<FormsListViewModel> implements View.OnClickListener {

    public static FormsListFragment newInstance() {
        return new FormsListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forms_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridLayout grid = view.findViewById(R.id.cardsContainer);

        List<Version> formVersions = viewModel.getFormVersions();
        grid.removeAllViews();

        for (Version version: formVersions) {
            FormSelectorCard card = new FormSelectorCard(view.getContext());
            card.setLetter(getString(viewModel.getLetterForFormVersion(version)));
            card.setText(getString(viewModel.getDescriptionForFormVersion(version)));
            card.setOnClickListener(this);
            card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, FormRenderer.dpToPx(150, getResources())));
            card.setTag(version);

            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            layoutParams.height = FormRenderer.dpToPx(150, getResources());
            layoutParams.width = 0;

            grid.addView(card, layoutParams);
        }

        FormSelectorCard card = new FormSelectorCard(view.getContext());
        card.setIcon(R.drawable.ic_notes);
        card.setText(getString(R.string.form_notes));
        card.setOnClickListener(this);

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        layoutParams.height = FormRenderer.dpToPx(150, getResources());
        layoutParams.width = 0;

        grid.addView(card, layoutParams);

        setBranchBar((ChangeBranchBarLayout) view.findViewById(R.id.change_branch_bar));
    }

    private void setBranchBar(ChangeBranchBarLayout barLayout) {
        barLayout.setBranchText(Preferences.getCountyCode() + " " + Preferences.getBranchNumber());
        barLayout.setChangeBranchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBackUntil(BRANCH_SELECTION_BACKSTACK_INDEX);
            }
        });
    }

    private void showForm(Form form) {
        if (form != null && form.getSections() != null && !form.getSections().isEmpty()) {
            navigateTo(QuestionsOverviewFragment.newInstance(form.getId()));
        } else {
            Toast.makeText(getActivity(), getString(R.string.error_no_form_data), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_forms_list);
    }

    @Override
    protected void setupViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(FormsListViewModel.class);
    }

    @Override
    public void onClick(View v) {
        if (!(v instanceof FormSelectorCard)) {
            return;
        }

        Object tag = v.getTag();

        if (!(tag instanceof Version)) {
            navigateTo(AddNoteFragment.newInstance());
        } else {
            showForm(Data.getInstance().getForm(((Version) tag).getKey()));
        }
    }
}

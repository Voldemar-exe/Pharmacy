package com.example.pharmacy.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmacy.R;
import com.example.pharmacy.databinding.FragmentMedicineListBinding;
import com.example.pharmacy.model.Medicine;
import com.example.pharmacy.model.MedicineDatabase;
import com.example.pharmacy.model.MedicineFiltration;
import com.example.pharmacy.ui.interfaces.OnCheckIsFavorite;
import com.example.pharmacy.ui.interfaces.OnMedicineClickListener;
import com.example.pharmacy.utils.MedicineListAdapter;
import com.example.pharmacy.utils.UserDataManager;
import com.example.pharmacy.viewmodel.MedicineListViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class MedicineListFragment
        extends Fragment
        implements OnMedicineClickListener, OnCheckIsFavorite {

    private FragmentMedicineListBinding binding;
    private MedicineListViewModel medicineListViewModel;
    private MedicineListAdapter medicineAdapter;
    private MedicineFiltration savedFiltration;
    private FirebaseAuth mAuth;
    private static final int HISTORY_MAX_SIZE = 50;
    private ViewModelProvider.Factory viewModelFactory;
    private int savedScrollPosition = 0;


    public MedicineListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedFiltration = new MedicineFiltration();
        mAuth = FirebaseAuth.getInstance();
        viewModelFactory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new MedicineListViewModel(
                        MedicineDatabase.getInstance(requireContext()).medicineDao()
                );
            }
        };
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(requireView()).popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentMedicineListBinding.inflate(inflater, container, false);
        if (savedScrollPosition != 0) {
            binding.medicationsList.smoothScrollToPosition(savedScrollPosition);
        }
        medicineAdapter = new MedicineListAdapter(this, this);

        medicineListViewModel = new ViewModelProvider(this, viewModelFactory)
                .get(MedicineListViewModel.class);
        medicineListViewModel
                .getMedicines()
                .observe(getViewLifecycleOwner(), medicineAdapter::updateItems);
        binding.medicationsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.medicationsList.setAdapter(medicineAdapter);
        binding.medicationsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                savedScrollPosition = layoutManager.findFirstVisibleItemPosition();
            }
        });
        binding.btnFilter.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("filter", savedFiltration);
            Navigation.findNavController(requireView())
                    .navigate(
                            R.id.action_MedicineListFragment_to_filterBottomSheetFragment,
                            bundle
                    );
        });
        binding.textToSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (medicineListViewModel.getMedicines().isInitialized()) {
                    medicineAdapter.updateItems(medicineListViewModel
                            .getFilteredMedicines(new MedicineFiltration(
                                    newText,
                                    null,
                                    null,
                                    null,
                                    null)
                            )
                    );
                }
                return false;
            }
        });

        return binding.getRoot();
    }

    private void addTestMedicines() {
        ArrayList<Medicine> medicineList = new ArrayList<>();
        String[] medicineArray = getResources().getStringArray(R.array.medicine);
        String[] typeArray = getResources().getStringArray(R.array.medicine_types);
        String[] medicineDetails = getResources().getStringArray(R.array.medicine_details);
        for (int i = 0; i < medicineArray.length; i++) {
            String[] details = String.valueOf(
                    Html.fromHtml(
                            medicineDetails[i],
                            Html.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE
                    )
            ).split("\n");
            medicineList.add(new Medicine(
                    medicineArray[i],
                    details[2],
                    details[3],
                    details[4],
                    details[5],
                    typeArray[i]
            ));
        }
        new Thread(() -> {
            for (Medicine medicine : medicineList) {
                medicineListViewModel.insertMedicine(medicine);
            }
        }).start();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences =
                requireActivity().getPreferences(Context.MODE_PRIVATE);
        boolean isFirstLaunch =
                sharedPreferences.getBoolean("first_launch", true);

        if (isFirstLaunch) {
            addTestMedicines();
            sharedPreferences.edit().putBoolean("first_launch", false).apply();
        }
        medicineListViewModel.getMedicines().observe(getViewLifecycleOwner(), medicines -> {
            if (getArguments() != null && getArguments().containsKey("filter")) {
                MedicineFiltration medicineFiltration =
                        (MedicineFiltration) getArguments().getSerializable("filter");
                savedFiltration = medicineFiltration;
                if (medicineFiltration != null) {
                    medicineAdapter.updateItems(
                            medicineListViewModel.getFilteredMedicines(medicineFiltration)
                    );
                }
            }
        });
    }

    @Override
    public void onAddToFavoritesClick(Medicine medicine) {
        if (mAuth.getCurrentUser() != null) {
            UserDataManager userManager = UserDataManager.getInstance(requireContext());
            userManager.readFavoritesMedicine();
            Set<Medicine> favorites = userManager.getFavoritesFromSharedPreferences();

            if (favorites == null) {
                favorites = new HashSet<>();
            }

            if (favorites.contains(medicine)) {
                favorites.remove(medicine);
            } else {
                favorites.add(medicine);
            }
            userManager.saveFavoritesToSharedPreferences(favorites);
            userManager.updateFavoriteMedicine(favorites);
            int position = medicineAdapter.getMedicineList().indexOf(medicine);
            if (position != -1) {
                medicineAdapter.notifyItemChanged(position);
            }

        } else {
            Toast.makeText(requireContext(), "Зарегистрируйтесь", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMedicineCardClick(Medicine medicine) {
        if (mAuth.getCurrentUser() != null) {
            UserDataManager userManager = UserDataManager.getInstance(requireContext());
            userManager.readHistoryMedicine();
            Deque<Medicine> history = userManager.getHistoryFromSharedPreferences();
            if (history != null) {
                history.remove(medicine);
            } else {
                history = new ArrayDeque<>();
            }
            history.addFirst(medicine);
            while (history.size() > HISTORY_MAX_SIZE) {
                history.removeLast();
            }

            userManager.saveHistoryToSharedPreferences(history);
            userManager.updateHistoryMedicine(history);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("medicine", medicine);
        Navigation.findNavController(requireView())
                .navigate(
                        R.id.action_MedicineListFragment_to_medicineDetailFragment,
                        bundle
                );
    }

    @Override
    public boolean isFavorite(Medicine medicine) {
        UserDataManager userManager = UserDataManager.getInstance(requireContext());
        userManager.readFavoritesMedicine();
        Set<Medicine> favorites = userManager.getFavoritesFromSharedPreferences();
        if (favorites != null){
            return favorites.contains(medicine);
        }
        return false;
    }
}
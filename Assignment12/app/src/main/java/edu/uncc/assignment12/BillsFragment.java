package edu.uncc.assignment12;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.uncc.assignment12.databinding.FragmentBillsBinding;

public class BillsFragment extends Fragment {
    public BillsFragment() {
        // Required empty public constructor
    }

    FragmentBillsBinding binding;

    private ArrayList<Bill> mBills = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBillsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(layoutManager);
        BillsFragment.BillsAdapter adapter = new BillsFragment.BillsAdapter(mBills, mListener);
        binding.recyclerView.setAdapter(adapter);
        mBills.clear();
        mBills.addAll(mListener.getAllBills());

        binding.buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.clearAllBills();
            }
        });

        binding.buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoCreateBill();
            }
        });
    }

    BillsListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BillsListener) {
            mListener = (BillsListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement BillsListener");
        }
    }


    interface BillsListener {
        void goToBillSummary(Bill bill);
        void goToEditBill(Bill bill);
        ArrayList<Bill> getAllBills();
        void gotoCreateBill();
        void clearAllBills();
    }

    class BillsAdapter extends RecyclerView.Adapter<BillsAdapter.BillsViewHolder>{

        public BillsAdapter(ArrayList<Bill> data, BillsListener listener) {
            mBills = data;
            mListener = listener;
        }

        @NonNull
        @Override
        public BillsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.bill_layout, parent, false);  // Create a new layout called item_genre.xml
            return new BillsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BillsViewHolder holder, int position) {
            Bill bill = mBills.get(position);
            holder.bind(bill);
        }

        @Override
        public int getItemCount() {
            return mBills.size();
        }

        class BillsViewHolder extends RecyclerView.ViewHolder{
            FragmentBillsBinding mBinding;
            TextView billName;
            TextView billAmount;
            TextView billDiscount;
            TextView billTotal;
            TextView billDate;
            TextView billType;
            public BillsViewHolder(View itemView) {
                super(itemView);
                billName = itemView.findViewById(R.id.billName);
                billAmount = itemView.findViewById(R.id.billAmount);
                billDiscount = itemView.findViewById(R.id.billDiscount);
                billTotal = itemView.findViewById(R.id.billTotal);
                billDate = itemView.findViewById(R.id.billDate);
                billType = itemView.findViewById(R.id.billType);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        mListener.goToBillSummary(mBills.get(position));
                    }
                });
            }
            public void bind(Bill bill) {
                billName.setText(bill.getName());
                billAmount.setText("$" + String.valueOf(bill.getAmount()));
                billDiscount.setText(String.valueOf(bill.getDiscount()) + "%");
                double discountAmount = bill.getAmount() * (bill.getDiscount() * .01);
                double total = bill.getAmount() - discountAmount;
                billTotal.setText("$" + String.valueOf(total));
                billDate.setText(String.valueOf(bill.getBillDate()));
                billType.setText(bill.getCategory());
            }
        }
    }
}
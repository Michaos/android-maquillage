package brostore.maquillage.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import brostore.maquillage.R;
import brostore.maquillage.dao.Product;
import brostore.maquillage.dao.User;
import brostore.maquillage.manager.UserManager;
import brostore.maquillage.view.MainActivity;
import brostore.maquillage.wrapper.BasketWrapper;

public class BasketAdapterRight extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<Product> myProducts;
    private ArrayList<Integer> myQuantities;
    private BasketWrapper wrapper;
    private int pos;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {

            int qty = Integer.parseInt(wrapper.getArticleQty().getText().toString());

            System.out.println("DEBUG qty .:" + qty + ":.");
            /*
            if (qty == 0) {
                UserManager.getInstance(mContext).removeFromBasket(myProducts.get(pos));
                ((MainActivity) mContext).refreshBasket();
            } else if (qty < myQuantities.get(pos)) {
                //todo
            } else {
                UserManager.getInstance(mContext).addInBasket(myProducts.get(pos), qty);
                ((MainActivity) mContext).refreshBasket();
            }
            */
        }
    };

    public BasketAdapterRight(Context context, User myUser) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        myProducts = myUser.getBasket();
        myQuantities = myUser.getQuantities();
    }

    @Override
    public int getCount() {
        return myProducts.size();
    }

    @Override
    public Product getItem(int position) {
        return myProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return myProducts.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = inflater.inflate(R.layout.item_basket, null);
            wrapper = new BasketWrapper(row);
            row.setTag(wrapper);
        } else {
            wrapper = (BasketWrapper) row.getTag();
        }

        final Product p = myProducts.get(position);

        if (p.getBitmapImage() == null) {
            wrapper.getArticleImage().setImageResource(R.drawable.maquillage);
        } else {
            wrapper.getArticleImage().setImageBitmap(p.getBitmapImage());
        }

        wrapper.getArticleName().setText(p.getName());

        double totalPrice = p.getReducedPrice() * myQuantities.get(position);
        wrapper.getArticleTotalPrice().setText(String.format("%.2f", totalPrice) + "€");

        // delete button
        wrapper.getBtnDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance(mContext).removeFromBasket(myProducts.get(position));
                ((MainActivity) mContext).refreshBasket();
            }
        });

        wrapper.getArticleQty().setText(myQuantities.get(position) + "");

        /*wrapper.getArticleQty().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {

                    pos = position;
                    handler.sendEmptyMessageDelayed(0, 1000);


                    /*if (qty == 0) {
                        UserManager.getInstance(mContext).removeFromBasket(myProducts.get(position));
                        ((MainActivity) mContext).refreshBasket();
                    } else if (qty < myQuantities.get(position)) {
                        //todo
                    } else {
                        UserManager.getInstance(mContext).addInBasket(myProducts.get(position), qty);
                        ((MainActivity) mContext).refreshBasket();
                    }*/

                    /*String qty = "";

                    switch (keyCode) {
                        case KeyEvent.KEYCODE_0:
                            qty = wrapper.getArticleQty().getText().toString();
                            break;
                        case KeyEvent.KEYCODE_1:
                            qty = wrapper.getArticleQty().getText().toString();
                            break;
                        case KeyEvent.KEYCODE_2:
                            qty = wrapper.getArticleQty().getText().toString();
                            break;
                        case KeyEvent.KEYCODE_3:
                            qty = wrapper.getArticleQty().getText().toString();
                            break;
                        case KeyEvent.KEYCODE_4:
                            qty = wrapper.getArticleQty().getText().toString();
                            break;
                        case KeyEvent.KEYCODE_5:
                            qty = wrapper.getArticleQty().getText().toString();
                            break;
                        case KeyEvent.KEYCODE_6:
                            qty = wrapper.getArticleQty().getText().toString();
                            break;
                        case KeyEvent.KEYCODE_7:
                            qty = wrapper.getArticleQty().getText().toString();
                            break;
                        case KeyEvent.KEYCODE_8:
                            qty = wrapper.getArticleQty().getText().toString();
                            break;
                        case KeyEvent.KEYCODE_9:
                            qty = wrapper.getArticleQty().getText().toString();
                            break;
                        case KeyEvent.KEYCODE_DEL:
                            qty = wrapper.getArticleQty().getText().toString();
                            break;
                        case KeyEvent.KEYCODE_ENTER:
                            System.out.println("DEBUG QTY 1 .:" + qty + ":.");
                            break;
                    }

                }
                return false;
            }
        });


        /*
        // Spinner Quantity
        List<String> list = new ArrayList<String>();
        for (int i = 1; i <= 10; i++) {
            list.add(i+"");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        wrapper.getArticleQuantitySpinner().setAdapter(dataAdapter);
        wrapper.getArticleQuantitySpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spinnerPosition, long id) {
                if (myQuantities.get(position) != spinnerPosition) {
                    UserManager.getInstance(mContext).getUser().getQuantities().set(position, spinnerPosition);
                    updateListView();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        wrapper.getArticleQuantitySpinner().setSelection(myQuantities.get(position));*/

        return row;
    }
}
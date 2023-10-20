//package com.gary.config;
//
//
//import com.gary.model.Inventory;
//import com.thoughtworks.xstream.converters.Converter;
//import com.thoughtworks.xstream.converters.MarshallingContext;
//import com.thoughtworks.xstream.converters.UnmarshallingContext;
//import com.thoughtworks.xstream.io.HierarchicalStreamReader;
//import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
//
//public class InventoryConverter implements Converter {
//
//    @Override
//    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext) {
//
//    }
//
//    @Override
//    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
//        hierarchicalStreamReader.moveDown();
//        Inventory inventory = new Inventory();
//        inventory.
//        return null;
//    }
//
//    @Override
//    public boolean canConvert(Class aClass) {
//        return aClass.equals(Inventory.class);
//    }
//}

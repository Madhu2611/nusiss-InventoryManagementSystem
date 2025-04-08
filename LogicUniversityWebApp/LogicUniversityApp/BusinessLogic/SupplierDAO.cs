using LogicUniversityApp.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace LogicUniversityApp.BusinessLogic
{
    public class SupplierDAO
    {
        public static List<Supplier> GetSupplierList()
        {
            return DbFactory.Instance.context.Suppliers.ToList();
        }

        public static Supplier FindSupplierById(string id)
        {
            return DbFactory.Instance.context.Suppliers.Find(id);
        }

        public static Boolean CreateSupplier(Supplier s)
        {
            try
            {
                DbFactory.Instance.context.Suppliers.Add(s);
                DbFactory.Instance.context.SaveChanges();
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }

        public static Boolean EditSupplier(Supplier update)
        {
            try
            {
                Supplier supplier = FindSupplierById(update.supplierId);
                supplier.address = update.address;
                supplier.contactName = update.contactName;
                supplier.email = update.email;
                supplier.supplierName = update.supplierName;
                supplier.phone = update.phone;
                supplier.fax = update.fax;
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }

        public static Boolean DeleteSupplier(Supplier s)
        {
            try
            {
                DbFactory.Instance.context.Suppliers.Remove(s);
                DbFactory.Instance.context.SaveChanges();
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }
    }
}
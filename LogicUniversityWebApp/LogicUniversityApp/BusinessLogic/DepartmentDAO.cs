using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using LogicUniversityApp.Models;
using LogicUniversityApp.ViewModel;

namespace LogicUniversityApp
{
    public class DepartmentDAO
    {
        public static List<Department> GetDepartmentsToDisburse()
        {
            List<Department> deps = DbFactory.Instance.context.Departments.AsNoTracking().
                Where(x => x.RequestLists.Where(y => y.status == "approved").FirstOrDefault() != null
                && x.RequestLists.Where(p => p.RequestDetails.Where(q => q.status == "preparing").FirstOrDefault() != null).FirstOrDefault() != null).ToList();
            return deps;
        }
        public static List<DepartmentViewModel> GetDepartments()
        {
            List<DepartmentViewModel> deps = DbFactory.Instance.context.Departments.
                Select(x => new DepartmentViewModel()
                {
                    departmentName = x.departmentName,
                    collectionPointId = x.collectionPointId,
                    collectionPointDescription = x.CollectionPoint.collectionPointDescription,

                    departmentId = x.departmentId,
                    departmentPhone = x.departmentPhone,
                    departmentRepName = x.Staff1.staffName
                }).ToList();
            return deps;
        }

        public static DepartmentViewModel GetDepartmentInfo(string departmentId)
        {
            DepartmentViewModel dep = null;
            dep = DbFactory.Instance.context.Departments.Where(x => x.departmentId == departmentId).
                Select(x => new DepartmentViewModel()
                {
                    departmentName = x.departmentName,
                    collectionPointId = x.collectionPointId,
                    collectionPointDescription = x.CollectionPoint.collectionPointDescription,

                    departmentId = x.departmentId,
                    departmentPhone = x.departmentPhone,
                    departmentRepName = x.Staff1.staffName
                }).FirstOrDefault();
            return dep;
        }

        public static Department GetDepartmentByUserId(string userId)
        {
            Staff staff = StaffDepartmentDAO.GetStaffByUserId(userId);
            Department dep = DbFactory.Instance.context.Departments.Where(x => x.departmentId == staff.departmentId).First();
            return dep;
        }

        public static Boolean UpdateDepartmentContacts(string depId, int staffId, int phoneNumber, int fax)
        {
            try
            {
                Department dep = StaffDepartmentDAO.getDepartmentById(depId);
                dep.staffIdContact = staffId;
                dep.departmentPhone = phoneNumber;
                dep.departmentFax = fax;
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
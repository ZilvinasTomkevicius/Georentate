using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using ClassLibrary.BusinessEntities;
using ClassLibrary.BusinessServices;

namespace API.Controllers
{
    public class AppInfoController : ApiController
    {
        IAppInfoServices appInfoServices = new AppInfoServices();

        /// <summary>
        /// AppInfo.Add
        /// </summary>
        /// <param name="appInfo"></param>
        /// <returns></returns>
        [HttpPost]
        public HttpResponseMessage Add(AppInfo appInfo)
        {
            try
            {
                appInfoServices.Add(appInfo);

                return Request.CreateResponse(HttpStatusCode.OK);
            }

            catch(Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }

        /// <summary>
        /// AppInfo.Update
        /// </summary>
        /// <param name="appInfo"></param>
        /// <returns></returns>
        [HttpPut]
        public HttpResponseMessage Update(AppInfo appInfo)
        {
            try
            {
                appInfoServices.Update(appInfo);

                return Request.CreateResponse(HttpStatusCode.OK);
            }

            catch(Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }

        /// <summary>
        /// AppInfo.Get
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public HttpResponseMessage Get()
        {
            try
            {
                AppInfo appInfo = appInfoServices.Get();

                return Request.CreateResponse(HttpStatusCode.OK, appInfo);
            }

            catch(Exception e)
            {
                return Request.CreateResponse(HttpStatusCode.InternalServerError, e.Message);
            }
        }
    }
}
